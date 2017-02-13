package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.core.eventsource.annotation.NoCache;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import freemarker.cache.ClassTemplateLoader;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.sse.ServerSentEventConnection;
import io.undertow.server.handlers.sse.ServerSentEventHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.util.ImmediateInstanceHandle;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import ro.isdc.wro.http.ConfigurableWroFilter;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.github.avarabyeu.jashing.utils.StringUtils.substringBefore;

/**
 * HTTP Server Controller. Bootstraps server and specifies all needed mappings and request handlers
 *
 * @author avarabyeu
 */
class JashingServer extends AbstractIdleService {

    public static final AttachmentKey<RateLimiter> RATE_LIMITER_KEY = AttachmentKey.create(RateLimiter.class);

    private final Integer port;

    private final EventBus eventBus;
    private final Gson gson;
    private final Optional<Long> timeout;

    private ServerSentEventHandler sseHandler;
    private Undertow server;

    public JashingServer(Integer port, EventBus eventBus, Gson gson, Optional<Long> timeout) {
        this.port = port;
        this.eventBus = eventBus;
        this.gson = gson;
        this.timeout = timeout;
    }

    @Override
    protected void startUp() throws Exception {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(
                freemarker.template.Configuration.VERSION_2_3_23);
        configuration.setTemplateLoader(new ClassTemplateLoader(Jashing.class, "/"));

        ConcurrentMap<String, JashingEvent> eventCache = Maps.newConcurrentMap();

        sseHandler = Handlers
                .serverSentEvents((connection, lastEventId) -> {
                    timeout.ifPresent(timeout -> {
                        connection.putAttachment(RATE_LIMITER_KEY, RateLimiter.create(timeout));
                        eventCache.values().forEach(serverSentEvent -> sendEvent(connection, serverSentEvent));
                    });
                });

        eventBus.register(new Consumer<JashingEvent>() {
            @Subscribe
            @Override
            public void accept(JashingEvent serverSentEvent) {
                if (!serverSentEvent.getClass().isAnnotationPresent(NoCache.class)) {
                    eventCache.put(serverSentEvent.getId(), serverSentEvent);
                }
                sseHandler.getConnections().parallelStream().forEach(c -> sendEvent(c, serverSentEvent));
            }
        });

        ResourceHandler widgetsHandler = Handlers.resource(
                new ClassPathResourceManager(Thread.currentThread().getContextClassLoader(),
                        "statics"));

        RoutingHandler routingHandler = Handlers.routing()
                .get("/views/{widget}", exchange -> {

                    exchange.setStatusCode(StatusCodes.FOUND);
                    String widgetName = exchange.getQueryParameters().get("widget").getFirst();
                    exchange.getResponseHeaders().put(Headers.LOCATION,
                            "/assets/widgets/" + substringBefore(widgetName, ".html") + "/" + widgetName);
                    exchange.endExchange();

                })
                .get("/{dashboard}", exchange -> {
                    StringWriter out = new StringWriter();
                    configuration.getTemplate(
                            "/views/dashboards/" + exchange.getQueryParameters()
                                    .get("dashboard").poll()
                                    + ".ftl.html").process(Collections.EMPTY_MAP, out);

                    exchange.getResponseSender().send(out.toString());
                })
                .get("/events", sseHandler)
                .get("/", Handlers.redirect("/sample"));

        HttpHandler rootHandler = Handlers.path(routingHandler)
                .addPrefixPath("/assets", widgetsHandler)
                .addPrefixPath("/compiled", getAggregationHandler());

        server = Undertow.builder().addHttpListener(port, "0.0.0.0")
                .setHandler(rootHandler).build();

        server.start();
    }

    private void sendEvent(ServerSentEventConnection c, JashingEvent serverSentEvent) {
        //wait if there is rate limiter
        Optional.ofNullable(c.getAttachment(RATE_LIMITER_KEY)).ifPresent(RateLimiter::acquire);
        c.send(gson.toJson(serverSentEvent));
    }

    /**
     * Uses Wro4j Filter to pre-process resources
     * Required for coffee scripts compilation and saas processing
     * Wro4j uses Servlet API so we make fake Servlet Deployment here to emulate servlet-based environment
     *
     * @return Static resources handler
     */
    private HttpHandler getAggregationHandler() throws ServletException {
        DeploymentInfo deploymentInfo = Servlets
                .deployment()
                .setClassLoader(JashingServer.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName("jashing")
                .addFilterUrlMapping("wro4j", "/*", DispatcherType.REQUEST)
                .addFilter(Servlets.filter("wro4j", ConfigurableWroFilter.class,
                        new InstanceFactory<ConfigurableWroFilter>() {
                            @Override
                            public InstanceHandle<ConfigurableWroFilter> createInstance()
                                    throws InstantiationException {
                                ConfigurableWroFilter filter = new ConfigurableWroFilter();
                                filter.setWroManagerFactory(new WroManagerFactory());
                                return new ImmediateInstanceHandle<>(filter);
                            }
                        }));
        DeploymentManager deployment = Servlets.defaultContainer().addDeployment(deploymentInfo);
        deployment.deploy();
        return deployment.start();
    }

    @Override
    protected void shutDown() throws Exception {
        /* close connections */
        sseHandler.getConnections().forEach(ServerSentEventConnection::shutdown);

        /* stop the server */
        server.stop();

    }
}

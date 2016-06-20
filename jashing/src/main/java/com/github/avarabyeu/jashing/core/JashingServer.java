package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.utils.StringUtils;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import freemarker.cache.ClassTemplateLoader;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicates;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.PredicateHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.sse.ServerSentEventHandler;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * HTTP Server Controller. Bootstraps server and specifies all needed mappings and request handlers
 *
 * @author avarabyeu
 */
class JashingServer extends AbstractIdleService {

    public static final AttachmentKey<RateLimiter> RATE_LIMITER_KEY = AttachmentKey.create(RateLimiter.class);
    /* 4567 is default Spart port */
    private final Integer port;

    private final EventBus eventBus;
    private final Gson gson;
    private final Optional<Long> timeout;

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

        ServerSentEventHandler sshHandler = Handlers
                .serverSentEvents((connection, lastEventId) -> {
                    connection
                            .putAttachment(RATE_LIMITER_KEY, RateLimiter.create(0.05));
                });

        eventBus.register(new Consumer<JashingEvent>() {
            @Subscribe
            @Override
            public void accept(JashingEvent serverSentEvent) {
                sshHandler.getConnections()
                        .parallelStream().forEach(c -> {
                    //wait if there is rate limiter
                    Optional.ofNullable(c.getAttachment(RATE_LIMITER_KEY)).ifPresent(RateLimiter::acquire);
                    c.send(gson.toJson(new ServerSentEvent<>(null, serverSentEvent)));
                });
            }
        });

        ResourceHandler staticsHandler = Handlers.resource(
                new ClassPathResourceManager(Thread.currentThread().getContextClassLoader(),
                        "statics"));

        RoutingHandler routingHandler = Handlers.routing()
                .get("/views/{widget}", exchange -> {
                    exchange.setStatusCode(StatusCodes.FOUND);
                    String widgetName = exchange.getQueryParameters().get("widget").getFirst();
                    exchange.getResponseHeaders().put(Headers.LOCATION,
                            "/widgets/" + StringUtils.substringBefore(widgetName, ".html") + "/"
                                    + widgetName);
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
                .get("/events", sshHandler)
                .get("/", Handlers.redirect("/sample"));

        PredicateHandler handler = Handlers
                .predicate(Predicates.or(Predicates.prefix("/assets"), Predicates.prefix("/widgets")), staticsHandler,
                        routingHandler);

        server = Undertow.builder().addHttpListener(port, "127.0.0.1")
                .setHandler(handler).build();

        server.start();
    }

    @Override
    protected void shutDown() throws Exception {
        /* stop the server */
        server.stop();

    }
}

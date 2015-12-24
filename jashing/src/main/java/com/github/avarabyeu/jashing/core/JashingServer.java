package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.utils.StringUtils;
import com.google.common.base.Throwables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.gson.Gson;
import freemarker.cache.ClassTemplateLoader;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PredicateHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.sse.ServerSentEventHandler;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import spark.Spark;

import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * HTTP Server Controller. Bootstraps server and specifies all needed mappings and request handlers
 *
 * @author avarabyeu
 */
class JashingServer extends AbstractIdleService {

    /* 4567 is default Spart port */
    private final Integer port;

    private final EventBus eventBus;
    private final Gson gson;

    public JashingServer(Integer port, EventBus eventBus, Gson gson) {
        this.port = port;
        this.eventBus = eventBus;
        this.gson = gson;
    }

    @Override
    protected void startUp() throws Exception {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        configuration.setTemplateLoader(new ClassTemplateLoader(Jashing.class, "/"));

        ServerSentEventHandler sshHandler = Handlers.serverSentEvents();
        eventBus.register(new Consumer<JashingEvent>() {
            @Subscribe
            @Override
            public void accept(JashingEvent serverSentEvent) {
                sshHandler.getConnections()
                        .forEach(c -> c.send(gson.toJson(new ServerSentEvent<>(null, serverSentEvent))));
            }
        });

        CompositeHandler compositeHandler = new CompositeHandler();
        compositeHandler
                .add(Predicates.path("/"), Handlers.redirect("/sample"))
                .add(Predicates.or(Predicates.prefix("/assets"), Predicates.prefix("/widgets")), Handlers.resource(
                        new ClassPathResourceManager(Thread.currentThread().getContextClassLoader(),
                                "statics")))
                .add(Predicates.prefix("/events"), sshHandler)
                .add(Predicates.truePredicate(), Handlers.pathTemplate()
                        .add("/views/{widget}", exchange -> {
                            exchange.setStatusCode(StatusCodes.FOUND);
                            String widgetName = exchange.getQueryParameters().get("widget").getFirst();
                            exchange.getResponseHeaders().put(Headers.LOCATION,
                                    "/widgets/" + StringUtils.substringBefore(widgetName, ".html") + "/"
                                            + widgetName);
                            exchange.endExchange();
                        })
                        .add("/{dashboard}", exchange -> {
                            StringWriter out = new StringWriter();
                            configuration.getTemplate(
                                    "/views/dashboards/" + exchange.getQueryParameters()
                                            .get("dashboard").poll()
                                            + ".ftl.html").process(Collections.EMPTY_MAP, out);

                            exchange.getResponseSender().send(out.toString());
                        }));

        PredicateHandler handler = Handlers
                .predicate(Predicates.or(Predicates.prefix("/assets"), Predicates.prefix("/widgets")),
                        Handlers.resource(
                                new ClassPathResourceManager(Thread.currentThread().getContextClassLoader(),
                                        "statics")),
                        Handlers.predicate(
                                Predicates.prefix("/events"), sshHandler, Handlers.pathTemplate()
                                        .add("/views/{widget}", exchange -> {
                                            exchange.setStatusCode(StatusCodes.FOUND);
                                            String widgetName = exchange.getQueryParameters().get("widget").getFirst();
                                            exchange.getResponseHeaders().put(Headers.LOCATION,
                                                    "/widgets/" + StringUtils.substringBefore(widgetName, ".html") + "/"
                                                            + widgetName);
                                            exchange.endExchange();
                                        })
                                        .add("/{dashboard}", exchange -> {
                                            StringWriter out = new StringWriter();
                                            configuration.getTemplate(
                                                    "/views/dashboards/" + exchange.getQueryParameters()
                                                            .get("dashboard").poll()
                                                            + ".ftl.html").process(Collections.EMPTY_MAP, out);

                                            exchange.getResponseSender().send(out.toString());
                                        })
                                        .add("/", Handlers.redirect("/sample"))));

        Undertow server = Undertow.builder().addHttpListener(port, "localhost").setHandler(handler).build();

        server.start();
    }

    @Override
    protected void shutDown() throws Exception {
        /* do nothing */
        Spark.stop();
    }

    public static class CompositeHandler implements HttpHandler {

        private Map<Predicate, HttpHandler> chain = new LinkedHashMap<>();

        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            chain.entrySet().stream()
                    .filter(entry -> entry.getKey().resolve(exchange))
                    .map(Map.Entry::getValue)
                    .forEachOrdered(handler -> {
                        try {
                            handler.handleRequest(exchange);
                        } catch (Exception e) {
                            Throwables.propagate(e);
                        }
                    });

        }

        public CompositeHandler add(Predicate p, HttpHandler h) {
            chain.put(p, h);
            return this;
        }

    }
}

package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.utils.StringUtils;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import freemarker.cache.ClassTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.*;
import org.webbitserver.handler.EmbeddedResourceHandler;
import org.webbitserver.rest.Rest;
import org.webbitserver.wrapper.EventSourceConnectionWrapper;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * WS controller based on Webbit server
 *
 * @author Andrei Varabyeu
 */
public class JashingWebbitServer extends AbstractIdleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JashingWebbitServer.class);

    @Inject
    private EventBus eventBus;

    /* optional timeout for sending updates for each client */
    @Named("timeout")
    @Inject(optional = true)
    private Long timeout;

    @Named("serverPort")
    @Inject
    private Integer serverPort;

    private freemarker.template.Configuration conf;

    private ServerSentEventHandler serverSentEventHandler;

    private WebServer webServer;


    @Override
    protected void startUp() throws Exception {
        conf = new freemarker.template.Configuration();
        conf.setTemplateLoader(new ClassTemplateLoader(Jashing.class, "/"));

        serverSentEventHandler = new ServerSentEventHandler(new Gson(), Optional.fromNullable(timeout));

        WebServer webServer = WebServers.createWebServer(serverPort)
                .add("/events", serverSentEventHandler);


        Rest rest = new Rest(webServer);

        rest.GET("/", (request, response, control) -> {
            rest.redirect(response, "/sample");
        });


        /**
         * Opens widget
         */
        rest.GET("/views/{widget}", (request, response, control) -> {
            String widget = Rest.stringParam(request, "widget");
            rest.redirect(response, "/widgets/" + StringUtils.substringBefore(widget, ".html") + "/" + widget);
        });

        /**
         * Opens dashboard
         */
        rest.GET("/{dashboard}", (request, response, control) -> {
            StringWriter writer = new StringWriter();
            try {
                conf.getTemplate("/views/dashboards/" + Rest.stringParam(request, "dashboard") + ".ftl.html").process(Collections.EMPTY_MAP, writer);
                response.content(writer.toString()).header(HttpHeaders.CONTENT_TYPE, MediaType.HTML_UTF_8.toString()).status(200).end();
            } catch (FileNotFoundException e){
                response.status(404).end();
            }


        });

        webServer.add(new EmbeddedResourceHandler("statics")); // path to web content
        this.webServer = webServer.start().get();
        eventBus.register(serverSentEventHandler);
    }


    @Override
    protected void shutDown() throws Exception {
        eventBus.unregister(serverSentEventHandler);
        webServer.stop();
    }


    public static class ServerSentEventHandler implements EventSourceHandler {

        private final Gson serializer;
        private final Optional<Long> timeout;
        private List<EventSourceConnection> connections;
        private Executor executor;


        public ServerSentEventHandler(Gson serializer, Optional<Long> timeout) {
            this.serializer = Preconditions.checkNotNull(serializer, "Serializer shouldn't be null");
            this.connections = Collections.synchronizedList(new ArrayList<>());
            this.timeout = timeout;
            this.executor = Executors.newFixedThreadPool(3, new ThreadFactoryBuilder()
                    .setNameFormat("SSE Event Handler Thread-%d").build());
        }


        @Override
        public void onOpen(EventSourceConnection connection) throws Exception {
            this.connections.add(timeout.isPresent() ? new LimitedConnection(connection, timeout.get()) : connection);
            LOGGER.debug("Connection opened. Count:{}", this.connections.size());
        }

        @Override
        public void onClose(EventSourceConnection connection) throws Exception {
            this.connections.remove(connection);
            LOGGER.debug("Connection closed. Count:{}", this.connections.size());
        }

        @Subscribe
        public void onEvent(JashingEvent event) {
            LOGGER.debug("Event fired: {}", event.getId());
            connections.forEach(connection -> executor.execute(() -> connection.send(new EventSourceMessage().data(serializer.toJson(event)))));
            LOGGER.debug("Event dispatched: {}", event.getId());
        }

        @Subscribe
        public void onShutdown(ShutdownEvent shutdownEvent) {
            connections.forEach(EventSourceConnection::close);
        }
    }

    public static class LimitedConnection extends EventSourceConnectionWrapper {
        private final RateLimiter limiter;

        public LimitedConnection(EventSourceConnection connection, Long timeout) {
            super(connection);
            this.limiter = RateLimiter.create((double) 1 / timeout);
        }

        public EventSourceConnectionWrapper send(EventSourceMessage event) {
            limiter.acquire();
            return super.send(event);
        }

        @Override
        public int hashCode() {
            return  originalControl().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return originalControl().equals(obj);
        }
    }
}

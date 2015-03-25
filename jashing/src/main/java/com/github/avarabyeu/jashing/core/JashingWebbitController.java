package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.utils.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import freemarker.cache.ClassTemplateLoader;
import org.webbitserver.*;
import org.webbitserver.handler.EmbeddedResourceHandler;
import org.webbitserver.rest.Rest;

import javax.annotation.Nonnull;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by andrey.vorobyov on 3/16/15.
 */
public class JashingWebbitController extends AbstractIdleService {

    private ServerSentEventHandler serverSentEventHandler;

    private final freemarker.template.Configuration conf;

    private WebServer webServer;

    private EventBus eventBus;


    @Inject
    public JashingWebbitController(@Nonnull EventBus eventBus) {
        this.eventBus = eventBus;

        conf = new freemarker.template.Configuration();
        conf.setTemplateLoader(new ClassTemplateLoader(Jashing.class, "/"));
    }


    @Override
    protected void startUp() throws Exception {
        serverSentEventHandler = new ServerSentEventHandler(new Gson());

        WebServer webServer = WebServers.createWebServer(8383)
                .add(new EmbeddedResourceHandler("statics")) // path to web content
                .add("/events", serverSentEventHandler);


        Rest rest = new Rest(webServer);

        rest.GET("/", (request, response, control) -> {
            Rest.redirect(response, "/sample");
        });


        /**
         * Opens widget
         */
        rest.GET("/views/{widget}", (request, response, control) -> {
            String widget = Rest.param(request, "widget");
            Rest.redirect(response, "/widgets/" + StringUtils.substringBefore(widget, ".html") + "/" + widget);
        });

        /**
         * Opens dashboard
         */
        rest.GET("/{dashboard}", (request, response, control) -> {
            StringWriter writer = new StringWriter();
            conf.getTemplate("/views/dashboards/" + Rest.param(request, "dashboard") + ".ftl.html").process(Collections.EMPTY_MAP, writer);
            response.content(writer.toString()).status(200).end();
        });

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
        private List<EventSourceConnection> connections;

        @Inject
        public ServerSentEventHandler(Gson serializer) {
            this.serializer = Preconditions.checkNotNull(serializer, "Serializer shouldn't be null");
            this.connections = new ArrayList<>();
        }


        @Override
        public void onOpen(EventSourceConnection connection) throws Exception {
            this.connections.add(connection);
        }

        @Override
        public void onClose(EventSourceConnection connection) throws Exception {
            this.connections.remove(connection);
        }

        @Subscribe
        public void onEvent(JashingEvent event) {
            connections.forEach(connection -> connection.send(new EventSourceMessage().data(serializer.toJson(event))));
        }

        @Subscribe
        public void onShutdown(ShutdownEvent shutdownEvent) {
            connections.forEach(EventSourceConnection::close);
        }
    }
}

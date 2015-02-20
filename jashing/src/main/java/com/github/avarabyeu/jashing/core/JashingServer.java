package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.core.subscribers.ServerSentEventHandler;
import com.github.avarabyeu.jashing.utils.StringUtils;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Provider;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * HTTP Server Controller. Bootstraps server and specifies all needed mappings and request handlers
 *
 * @author avarabyeu
 */
class JashingServer extends AbstractIdleService {


    /* 4567 is default Spart port */
    private final int port;

    private final Provider<ServerSentEventHandler> serverSentEventHandler;
    private final FreeMarkerEngine freemarkerEngine;

    public JashingServer(int port, @Nonnull Provider<ServerSentEventHandler> serverSentEventHandler) {
        this.port = port;
        this.serverSentEventHandler = serverSentEventHandler;

        freemarkerEngine = new FreeMarkerEngine();
        Configuration conf = new Configuration();
        conf.setTemplateLoader(new ClassTemplateLoader(Jashing.class, "/"));
        freemarkerEngine.setConfiguration(conf);
    }

    @Override
    protected void startUp() throws Exception {
        Spark.port(port);
        staticFileLocation("/statics");

        /**
         * Redirect to the default dashboard
         */
        get("/", (request, response) -> {
                    response.redirect("/sample");
                    return response;
                }
        );

        /**
         * Creates new SSE event handler which pushes events into response output stream
         */
        get("/events", (request, response) ->
                {
                    try {
                        serverSentEventHandler.get().handle(request, response);
                    } catch (IOException e) {
                        return null;
                    }
                    //return null anyway. handler takes care about response content
                    return null;
                }
        );

        /**
         * Opens widget
         */
        get("views/:widget", (request, response) -> {
                    /* path to widget is /widgets/{widget folder == widget name}/{widget name}.html */
                    response.redirect("/widgets/" + StringUtils.substringBefore(request.params(":widget"), ".html") + "/" + request.params(":widget"));
                    return response;
                }
        );

        /**
         * Opens dashboard
         */
        get("/:dashboard", (request, response) ->
                        new ModelAndView(Collections.EMPTY_MAP, "/statics/assets/views/dashboards/" + request.params(":dashboard") + ".ftl.html"), this.freemarkerEngine
        );


    }


    @Override
    protected void shutDown() throws Exception {
        /* do nothing */
    }
}

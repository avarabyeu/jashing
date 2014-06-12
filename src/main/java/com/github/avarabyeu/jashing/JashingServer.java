package com.github.avarabyeu.jashing;

import com.github.avarabyeu.jashing.subscribers.ServerSentEventHandler;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Provider;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.IOException;
import java.util.Collections;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * HTTP Server Controller. Bootstraps server and specifies all needed mappings and request handlers
 *
 * @author avarabyeu
 */
public class JashingServer extends AbstractIdleService {


    private final int port;
    private final Provider<ServerSentEventHandler> serverSentEventHandler;
    private FreeMarkerEngine freemarkerEngine;

    public JashingServer(int port, Provider<ServerSentEventHandler> serverSentEventHandler) {
        this.port = port;
        this.serverSentEventHandler = serverSentEventHandler;

        freemarkerEngine = new FreeMarkerEngine();
        Configuration conf = new Configuration();
        conf.setTemplateLoader(new ClassTemplateLoader(Jashing.class, "/"));
        freemarkerEngine.setConfiguration(conf);
    }

    @Override
    protected void startUp() throws Exception {
        Spark.setPort(port);
        staticFileLocation("/statics");

        get("/", (request, response) -> {
                    response.redirect("/sample");
                    return response;
                }
        );

        get("/events", (request, response) ->
                {
                    try {
                        serverSentEventHandler.get().handle(request, response);
                    } catch (IOException e) {
                        return null;
                    }
                    return null;
                }
        );

        get("views/:widget", (request, response) -> {
                    response.redirect("/widgets/" + StringUtils.substringBefore(request.params(":widget"), ".html") + "/" + request.params(":widget"));
                    return response;
                }
        );

        get("/:dashboard", (request, response) ->
                        new ModelAndView(Collections.EMPTY_MAP, "/statics/assets/views/dashboards/" + request.params(":dashboard") + ".ftl.html"), this.freemarkerEngine
        );


    }


    @Override
    protected void shutDown() throws Exception {

    }
}

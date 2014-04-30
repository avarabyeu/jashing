package com.github.avarabyeu.jashing;

import com.github.avarabyeu.jashing.subscribers.ServerSentEventHandler;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Provider;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerRoute;

import java.io.IOException;
import java.util.Collections;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * Created by andrey.vorobyov on 11/04/14.
 */
public class JashingServer extends AbstractIdleService {


    private final int port;
    private final Provider<ServerSentEventHandler> serverSentEventHandler;

    public JashingServer(int port, Provider<ServerSentEventHandler> serverSentEventHandler) {
        this.port = port;
        this.serverSentEventHandler = serverSentEventHandler;
    }

    @Override
    protected void startUp() throws Exception {
        Spark.setPort(port);
        staticFileLocation("/statics");

        get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {
                response.redirect("/sample");
                return response;
            }
        });

        get(new Route("/events") {
            @Override
            public Object handle(Request request, Response response) {

                try {
                    serverSentEventHandler.get().handle(request, response);
                } catch (IOException e) {
                    return null;
                }
                return null;
            }
        });

        get(new Route("views/:widget") {
            @Override
            public Object handle(Request request, Response response) {
                response.redirect("/widgets/" + StringUtils.substringBefore(request.params(":widget"), ".html") + "/" + request.params(":widget"));
                return response;
            }
        });

        get(new FreeMarkerRoute("/:dashboard") {
            @Override
            public Object handle(Request request, Response response) {
                Configuration conf = new Configuration();
                conf.setTemplateLoader(new ClassTemplateLoader(Jashing.class, "/"));
                setConfiguration(conf);
                return modelAndView(Collections.EMPTY_MAP, "/statics/assets/views/dashboards/" + request.params(":dashboard") + ".ftl.html");
            }
        });


    }


    @Override
    protected void shutDown() throws Exception {

    }
}

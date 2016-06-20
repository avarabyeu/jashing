package com.github.avarabyeu.jashing.http;

import com.google.common.collect.ImmutableMap;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * Router is a mapping between routes and their handlers
 *
 * @author Andrei Varabyeu
 */
public class Router {

    private Map<Route, RequestHandler> routes;

    private Router(Map<Route, RequestHandler> routes) {
        this.routes = routes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<RequestHandler> getHandler(Request request) {
        return routes.entrySet().stream().filter(e -> e.getKey().matches(request)).findFirst().map(Map.Entry::getValue);
    }

    public static class Builder {

        private ImmutableMap.Builder<Route, RequestHandler> routes = ImmutableMap.builder();

        public Builder path(Request.Method method, String route, RequestHandler handler) {
            routes.put(new RegexpRoute(method, route), handler);
            return this;
        }

        public Builder statics(Path path) {
            routes.put(new RegexpRoute(Request.Method.GET, ".*"), new StaticsHandler(path));
            return this;
        }

        public Builder resources(String basePath) {
            routes.put(new RegexpRoute(Request.Method.GET, ".*"), new ResourceHandler(basePath));
            return this;
        }

        public Router build() {
            return new Router(routes.build());
        }

    }
}

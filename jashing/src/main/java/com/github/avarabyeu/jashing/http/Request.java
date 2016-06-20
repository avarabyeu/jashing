package com.github.avarabyeu.jashing.http;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Andrei Varabyeu
 */
public class Request {

    private final HttpServletRequest delegate;

    public enum Method {
        OPTIONS,
        HEAD,
        GET,
        POST,
        PUT,
        PATCH,
        DELETE,
        TRACE,
        CONNECT
    }

    private Map<String, String> pathVariables;

    public Request(HttpServletRequest delegate) {
        this.delegate = delegate;
    }

    public Map<String, String> getHeaders() {
        return Utils.enumerationAsStream(delegate.getHeaderNames())
                .collect(Collectors.toMap(name -> name, delegate::getHeader));
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

    void setPathVariables(Map<String, String> pathVariables) {
        this.pathVariables = pathVariables;
    }

    public HttpServletRequest raw() {
        return this.delegate;
    }

    public Method getMethod() {
        return Method.valueOf(this.delegate.getMethod().toUpperCase());
    }

    public String getRequestUri() {
        return delegate.getRequestURI();
    }

}

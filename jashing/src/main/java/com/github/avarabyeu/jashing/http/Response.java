package com.github.avarabyeu.jashing.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Server response representation
 *
 * @author Andrei Varabyeu
 */
public class Response {

    private final HttpServletResponse delegate;

    public Response(HttpServletResponse delegate) throws IOException {
        this.delegate = delegate;
    }

    public Response content(String content) {
        try {
            this.delegate.getWriter().write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Response contentType(String content) {
        this.delegate.setContentType(content);
        return this;
    }

    public HttpServletResponse raw() {
        return this.delegate;
    }

    public Response statusCode(int statusCode) {
        this.delegate.setStatus(statusCode);
        return this;
    }

}

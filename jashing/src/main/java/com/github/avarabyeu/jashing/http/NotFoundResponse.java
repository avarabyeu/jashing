package com.github.avarabyeu.jashing.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by avarabyeu on 11/28/15.
 */
public class NotFoundResponse extends Response {

    public NotFoundResponse(HttpServletResponse delegate) throws IOException {
        super(delegate);
        super.statusCode(404);
        //message
    }

    @Override
    public Response statusCode(int statusCode) {
        throw new UnsupportedOperationException("Cannot change status of 404 response");
    }
}

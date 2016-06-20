package com.github.avarabyeu.jashing.http;

import java.io.IOException;

/**
 * Handles Request
 *
 * @author Andrei Varabyeu
 */
public interface RequestHandler {

    /**
     * Handles request and fills response
     *
     * @param request  Request to Server
     * @param response Response from Server
     */
    void handle(Request request, Response response) throws IOException;
}

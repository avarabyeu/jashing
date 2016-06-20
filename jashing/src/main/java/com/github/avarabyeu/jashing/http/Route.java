package com.github.avarabyeu.jashing.http;

/**
 * Route representation
 * 'Route' means HTTP Method + request path pair
 *
 * @author Andrei Varabyeu
 */
public abstract class Route {

    /**
     * Check whether giver request is matches to Route
     *
     * @param request Request
     * @return TRUE if route matches
     */
    protected abstract boolean matches(Request request);
}

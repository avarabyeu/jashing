package com.github.avarabyeu.jashing.core;

/**
 * Server Sent Event representation. Contains Event ID and data
 *
 * @author avarabyeu
 */
public class ServerSentEvent<T> {

    private String id;

    private T data;

    public ServerSentEvent(String id, T data) {
        this.data = data;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public T getData() {
        return data;
    }
}

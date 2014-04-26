package com.github.avarabyeu.jashing;

/**
 * Created by andrey.vorobyov on 18/04/14.
 */
public class ServerSentEvent<T> {

    private String id;

    private T data;

    public ServerSentEvent(String id, T data) {
        this.id = id;
        this.data = data;
    }


    public String getId() {
        return id;
    }

    public T getData() {
        return data;
    }
}

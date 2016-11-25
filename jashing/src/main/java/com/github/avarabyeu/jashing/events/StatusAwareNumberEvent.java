package com.github.avarabyeu.jashing.events;

/**
 * Created by avarabyeu on 11/24/16.
 */
public class StatusAwareNumberEvent extends NumberEvent {
    private final Status status;

    public StatusAwareNumberEvent(Status status, int current, int last) {
        super(current, last);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }


    public enum  Status{
        ok,
        danger,
        warning
    }

}

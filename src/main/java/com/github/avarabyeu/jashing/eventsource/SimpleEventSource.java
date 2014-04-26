package com.github.avarabyeu.jashing.eventsource;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractIdleService;

import javax.inject.Inject;

/**
 * Created by andrey.vorobyov on 25/04/14.
 */
abstract class SimpleEventSource<T> extends AbstractIdleService implements EventSource<T> {


    @Inject
    private EventBus eventBus;

    public void sendEvent(T event) {
        this.eventBus.post(event);
    }

    @Override
    protected void startUp() throws Exception {

    }

    @Override
    protected void shutDown() throws Exception {

    }
}

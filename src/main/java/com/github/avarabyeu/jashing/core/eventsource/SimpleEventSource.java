package com.github.avarabyeu.jashing.core.eventsource;

import com.github.avarabyeu.jashing.core.JashingEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractIdleService;

import javax.inject.Inject;

/**
 * @author Andrei Varabyeu
 */
abstract class SimpleEventSource<T extends JashingEvent> extends AbstractIdleService implements EventSource<T> {


    @Inject
    private EventBus eventBus;

    public void sendEvent(T event) {
        this.eventBus.post(event);
    }

    @Override
    protected void startUp() throws Exception {
        /* no any lifecycle-related logic */
    }

    @Override
    protected void shutDown() throws Exception {
        /* no any lifecycle-related logic */
    }
}

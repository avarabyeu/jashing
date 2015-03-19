package com.github.avarabyeu.jashing.core.eventsource;

import com.github.avarabyeu.jashing.core.JashingEvent;
import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

import javax.inject.Inject;

/**
 * @author Andrei Varabyeu
 */
abstract class SimpleEventSource<T extends JashingEvent> extends AbstractExecutionThreadService {


    /**
     * EventBus to send events
     */
    @Inject
    private EventBus eventBus;

    /**
     * ID of event this event source bound to
     */
    @EventId
    @Inject
    private String eventId;

    protected final void sendEvent(T t) {
        if (null != t) {
            t.setId(eventId);
            this.eventBus.post(t);
        }
    }

    @Override
    protected void startUp() throws Exception {
        /* no any lifecycle-related logic */
    }

    @Override
    protected void shutDown() throws Exception {
        /* no any lifecycle-related logic */
    }

    protected abstract T produceEvent();

    @Override
    protected String serviceName() {
        return "SimpleEventSource[eventID=" + eventId + "]";
    }
}

package com.github.avarabyeu.jashing.eventsource;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;

/**
 * Created by andrey.vorobyov on 25/04/14.
 */
public abstract class ScheduledEventSource<T> extends AbstractScheduledService implements EventSource<T> {

    @Inject
    private EventBus eventBus;


    @Override
    protected final void runOneIteration() throws Exception {
        sendEvent(produceEvent());
    }


    @Override
    public final void sendEvent(T t) {
        this.eventBus.post(t);
    }

    protected abstract T produceEvent();
}

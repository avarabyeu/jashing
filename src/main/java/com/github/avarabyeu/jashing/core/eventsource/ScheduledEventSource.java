package com.github.avarabyeu.jashing.core.eventsource;

import com.github.avarabyeu.jashing.core.JashingEvent;
import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.core.eventsource.annotation.Frequency;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled EventSource - produces events with specified time period. Basically, based on Guava's {@link com.google.common.util.concurrent.AbstractScheduledService}
 *
 * @author avarabyeu
 */
public abstract class ScheduledEventSource<T extends JashingEvent> extends AbstractScheduledService implements EventSource<T> {


    @Frequency
    @Inject
    private Duration period;

    @EventId
    @Inject
    private String eventId;

    @Inject
    private EventBus eventBus;


    @Override
    protected final void runOneIteration() throws Exception {
        T t = produceEvent();
        assert null != t;
        t.setId(eventId);
        sendEvent(t);
    }


    @Override
    public final void sendEvent(T t) {
        this.eventBus.post(t);
    }

    @Override
    final protected Scheduler scheduler() {
        return AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, period.toMillis(), TimeUnit.MILLISECONDS);
    }

    protected abstract T produceEvent();
}

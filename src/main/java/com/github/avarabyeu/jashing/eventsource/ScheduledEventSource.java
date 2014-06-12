package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.JashingEvent;
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

    private final Duration period;
    private final String eventId;

    @Inject
    private EventBus eventBus;

    public ScheduledEventSource(String eventId, Duration period) {
        this.period = period;
        this.eventId = eventId;
    }

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

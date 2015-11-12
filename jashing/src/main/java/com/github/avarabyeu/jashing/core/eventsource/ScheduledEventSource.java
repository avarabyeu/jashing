package com.github.avarabyeu.jashing.core.eventsource;

import com.github.avarabyeu.jashing.core.JashingEvent;
import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.core.eventsource.annotation.Frequency;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled EventSource - produces events with specified time period.
 * Basically, based on Guava's {@link com.google.common.util.concurrent.AbstractScheduledService}
 *
 * @author avarabyeu
 */
public abstract class ScheduledEventSource<T extends JashingEvent> extends AbstractScheduledService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledEventSource.class);

    /**
     * Time period between sending events
     */
    @Frequency
    @Inject
    private Duration period;

    /**
     * ID of event this event source bound to
     */
    @EventId
    @Inject
    private String eventId;

    /**
     * EventBus to send events
     */
    @Inject
    private EventBus eventBus;


    /**
     * Executes one iteration of {@link com.google.common.util.concurrent.AbstractScheduledService}
     * Sends event to event bus
     *
     * @throws Exception
     */
    @Override
    protected final void runOneIteration() throws Exception {
        try {
            sendEvent(produceEvent());
        } catch (Exception e){
            LOGGER.error(MessageFormatter.format("Cannot produce event with id {}", eventId).getMessage(), e);
        }

    }


    protected final void sendEvent(T t) {
        if (null != t) {
            t.setId(eventId);
            this.eventBus.post(t);
        }
    }

    @Override
    protected final Scheduler scheduler() {
        return AbstractScheduledService.Scheduler.newFixedDelaySchedule(3, period.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    protected final String serviceName() {
        return "ScheduledEventSource[eventID=" + eventId + "]";
    }

    protected abstract T produceEvent();
}

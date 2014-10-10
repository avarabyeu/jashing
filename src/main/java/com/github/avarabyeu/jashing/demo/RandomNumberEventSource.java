package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.NumberEvent;
import com.github.avarabyeu.jashing.eventsource.HandlesEvent;
import com.github.avarabyeu.jashing.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.eventsource.annotation.Frequency;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Random;

/**
 * Created by andrey.vorobyov on 31/05/14.
 */
@HandlesEvent(Events.NUMBER)
public class RandomNumberEventSource extends ScheduledEventSource<NumberEvent> {

    private Random r = new Random();

    private int lastValue;

    @Inject
    public RandomNumberEventSource(@EventId String eventId, @Frequency Duration period) {
        super(eventId, period);
    }

    @Override
    protected NumberEvent produceEvent() {
        int current = r.nextInt(1000);
        int previous = this.lastValue;
        this.lastValue = current;
        return new NumberEvent(current, previous);
    }


}

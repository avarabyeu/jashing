package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.NumberEvent;

import java.time.Duration;
import java.util.Random;

/**
 * Created by andrey.vorobyov on 31/05/14.
 */
@HandlesEvent(Events.NUMBER)
public class RandomNumberEventSource extends ScheduledEventSource<NumberEvent> {

    private Random r = new Random();

    private int lastValue;

    public RandomNumberEventSource(String eventId, Duration period) {
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

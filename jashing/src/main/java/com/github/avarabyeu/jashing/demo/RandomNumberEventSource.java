package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.NumberEvent;

import java.util.Random;

/**
 * @author Andrei Varabyeu
 */
@EventSource("random-number-event-source")
public class RandomNumberEventSource extends ScheduledEventSource<NumberEvent> {

    private final Random r = new Random();

    private int lastValue;


    @Override
    protected NumberEvent produceEvent() {
        int current = r.nextInt(1000);
        int previous = this.lastValue;
        this.lastValue = current;
        return new NumberEvent(current, previous);
    }


}

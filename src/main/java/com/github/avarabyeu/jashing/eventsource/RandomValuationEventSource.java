package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.ValuationEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrey.vorobyov on 30/04/14.
 */
public class RandomValuationEventSource extends ScheduledEventSource<ValuationEvent> {

    private Random r = new Random();

    private int lastValue;

    @Override
    protected ValuationEvent produceEvent() {
        int current = r.nextInt(1000);
        int previous = this.lastValue;
        this.lastValue = current;
        return new ValuationEvent(current, previous);
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 3l, TimeUnit.SECONDS);
    }
}

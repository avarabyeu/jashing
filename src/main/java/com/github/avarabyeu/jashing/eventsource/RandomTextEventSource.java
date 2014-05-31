package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.MeterEvent;
import com.github.avarabyeu.jashing.events.TextEvent;
import com.google.common.util.concurrent.AbstractScheduledService;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrey.vorobyov on 31/05/14.
 */
public class RandomTextEventSource extends ScheduledEventSource<TextEvent> {

    private Random r = new Random();

    @Override
    protected TextEvent produceEvent() {
        return new TextEvent("Hello world! [" + r.nextInt(100) + "]");
    }

    @Override
    protected Scheduler scheduler() {
        return AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, 3l, TimeUnit.SECONDS);
    }
}

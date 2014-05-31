package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.MeterEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author avarabyeu
 */
public class RandomMeterEventSource extends ScheduledEventSource<MeterEvent> {

    private Random r = new Random();

    @Override
    protected MeterEvent produceEvent() {
        return new MeterEvent((byte) r.nextInt(100));
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 3l, TimeUnit.SECONDS);
    }
}

package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.SynergyEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author avarabyeu
 */
public class RandomSynergyEventSource extends ScheduledEventSource<SynergyEvent> {

    private Random r = new Random();

    @Override
    protected SynergyEvent produceEvent() {
        return new SynergyEvent((byte) r.nextInt(100));
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 3l, TimeUnit.SECONDS);
    }
}

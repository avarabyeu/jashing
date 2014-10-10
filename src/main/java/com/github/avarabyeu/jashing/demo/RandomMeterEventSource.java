package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.core.eventsource.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.MeterEvent;

import java.util.Random;

/**
 * @author avarabyeu
 */
@HandlesEvent(Events.METER)
public class RandomMeterEventSource extends ScheduledEventSource<MeterEvent> {

    private Random r = new Random();

    @Override
    protected MeterEvent produceEvent() {
        return new MeterEvent((byte) r.nextInt(100));
    }


}

package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.MeterEvent;

import java.util.Random;

/**
 * @author avarabyeu
 */
@EventSource("random-meter-source")
public class RandomMeterEventSource extends ScheduledEventSource<MeterEvent> {

    private final Random r = new Random();

    @Override
    protected MeterEvent produceEvent() {
        return new MeterEvent((byte) r.nextInt(100));
    }


}

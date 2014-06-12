package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.MeterEvent;

import java.time.Duration;
import java.util.Random;

/**
 * @author avarabyeu
 */
@HandlesEvent(Events.METER)
public class RandomMeterEventSource extends ScheduledEventSource<MeterEvent> {

    private Random r = new Random();

    public RandomMeterEventSource(String eventId, Duration period) {
        super(eventId, period);
    }

    @Override
    protected MeterEvent produceEvent() {
        return new MeterEvent((byte) r.nextInt(100));
    }


}

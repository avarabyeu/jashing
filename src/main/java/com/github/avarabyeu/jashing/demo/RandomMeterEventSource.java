package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.MeterEvent;
import com.github.avarabyeu.jashing.eventsource.HandlesEvent;
import com.github.avarabyeu.jashing.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.eventsource.annotation.Frequency;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Random;

/**
 * @author avarabyeu
 */
@HandlesEvent(Events.METER)
public class RandomMeterEventSource extends ScheduledEventSource<MeterEvent> {

    private Random r = new Random();

    @Inject
    public RandomMeterEventSource(@EventId String eventId, @Frequency Duration period) {
        super(eventId, period);
    }

    @Override
    protected MeterEvent produceEvent() {
        return new MeterEvent((byte) r.nextInt(100));
    }


}

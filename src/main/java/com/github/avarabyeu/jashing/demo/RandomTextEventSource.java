package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.TextEvent;
import com.github.avarabyeu.jashing.core.eventsource.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.core.eventsource.annotation.Frequency;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Random;

/**
 * Created by andrey.vorobyov on 31/05/14.
 */
@HandlesEvent(Events.TEXT)
public class RandomTextEventSource extends ScheduledEventSource<TextEvent> {

    private Random r = new Random();


    @Inject
    public RandomTextEventSource(@EventId String eventId, @Frequency Duration period) {
        super(eventId, period);
    }


    @Override
    protected TextEvent produceEvent() {
        return new TextEvent("Hello world! [" + r.nextInt(100) + "]");
    }


}

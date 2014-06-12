package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.TextEvent;

import java.time.Duration;
import java.util.Random;

/**
 * Created by andrey.vorobyov on 31/05/14.
 */
@HandlesEvent(Events.TEXT)
public class RandomTextEventSource extends ScheduledEventSource<TextEvent> {

    private Random r = new Random();

    public RandomTextEventSource(String eventId, Duration period) {
        super(eventId, period);
    }


    @Override
    protected TextEvent produceEvent() {
        return new TextEvent("Hello world! [" + r.nextInt(100) + "]");
    }


}

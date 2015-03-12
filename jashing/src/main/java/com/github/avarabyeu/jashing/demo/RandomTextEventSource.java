package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.core.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.TextEvent;

import java.util.Random;

/**
 * @author Andrei Varabyeu
 */
@HandlesEvent(Events.TEXT)
public class RandomTextEventSource extends ScheduledEventSource<TextEvent> {

    private final Random r = new Random();


    @Override
    protected TextEvent produceEvent() {
        return new TextEvent("Hello world! [" + r.nextInt(100) + "]");
    }


}

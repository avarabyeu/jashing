package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.core.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.ProgressBarEvent;
import com.google.common.collect.ImmutableList;

import java.util.Random;

/**
 * @author Andrei Varabyeu
 */
@HandlesEvent(Events.PROGRESS_BAR)
public class RandomProgressBarEventSource extends ScheduledEventSource<ProgressBarEvent> {

    private static final Random RANDOM = new Random();

    @Override
    protected ProgressBarEvent produceEvent() {
        return new ProgressBarEvent("Demo Progress Bar", ImmutableList.
                <ProgressBarEvent.Item>builder()
                .add(new ProgressBarEvent.Item("First Bar!", ((Integer) RANDOM.nextInt(100)).byteValue()))
                .add(new ProgressBarEvent.Item("Second Bar!", ((Integer) RANDOM.nextInt(100)).byteValue()))
                .add(new ProgressBarEvent.Item("Third Bar!", ((Integer) RANDOM.nextInt(100)).byteValue()))
                .build());
    }
}

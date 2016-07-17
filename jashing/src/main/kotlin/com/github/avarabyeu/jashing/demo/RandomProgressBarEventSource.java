package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.ProgressBarEvent;
import com.google.common.collect.ImmutableList;

import java.util.Random;

/**
 * @author Andrei Varabyeu
 */
@EventSource("random-progress-bar-source")
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

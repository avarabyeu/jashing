package com.github.avarabyeu.jashing.demo;

import com.github.avarabyeu.jashing.core.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.GraphEvent;
import com.google.common.collect.EvictingQueue;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andrei Varabyeu
 */
@HandlesEvent(Events.GRAPH)
public class RandomGraphEventSource extends ScheduledEventSource<GraphEvent> {

    private Random random = new Random();

    private AtomicInteger counter = new AtomicInteger();

    private Queue<GraphEvent.Point> points = EvictingQueue.create(10);

    @Override
    protected GraphEvent produceEvent() {
        points.add(new GraphEvent.Point(counter.incrementAndGet(), random.nextInt(100)));
        return new GraphEvent(points);
    }
}

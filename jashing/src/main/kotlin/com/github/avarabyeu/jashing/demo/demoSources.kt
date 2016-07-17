package com.github.avarabyeu.jashing.demo

import com.github.avarabyeu.jashing.core.EventSource
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource
import com.github.avarabyeu.jashing.events.*
import com.google.common.collect.EvictingQueue
import com.google.common.collect.ImmutableList
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


private val RANDOM = Random()


@EventSource("random-graph-event-source")
class RandomGraphEventSource : ScheduledEventSource<GraphEvent>() {

    private val counter = AtomicInteger()

    private val points = EvictingQueue.create<GraphEvent.Point>(10)

    override fun produceEvent(): GraphEvent {
        points.add(GraphEvent.Point(counter.incrementAndGet(), RANDOM.nextInt(100)))
        return GraphEvent(points)
    }
}

@EventSource("random-meter-source")
class RandomMeterEventSource : ScheduledEventSource<MeterEvent>() {

    override fun produceEvent(): MeterEvent {
        return MeterEvent(RANDOM.nextInt(100).toByte())
    }
}

@EventSource("random-number-event-source")
class RandomNumberEventSource : ScheduledEventSource<NumberEvent>() {

    private var lastValue: Int = 0

    override fun produceEvent(): NumberEvent {
        val current = RANDOM.nextInt(1000)
        val previous = this.lastValue
        this.lastValue = current
        return NumberEvent(current, previous)
    }
}

@EventSource("random-progress-bar-source")
class RandomProgressBarEventSource : ScheduledEventSource<ProgressBarEvent>() {

    override fun produceEvent(): ProgressBarEvent {
        return ProgressBarEvent("Demo Progress Bar",
                ImmutableList.builder<ProgressBarEvent.Item>()
                        .add(ProgressBarEvent.Item("First Bar!", RANDOM.nextInt(100).toInt().toByte()))
                        .add(ProgressBarEvent.Item("Second Bar!", RANDOM.nextInt(100).toInt().toByte()))
                        .add(ProgressBarEvent.Item("Third Bar!", RANDOM.nextInt(100).toInt().toByte()))
                        .build())
    }
}

@EventSource("random-text-event-source")
class RandomTextEventSource : ScheduledEventSource<TextEvent>() {

    override fun produceEvent(): TextEvent {
        return TextEvent("Hello world! [" + RANDOM.nextInt(100) + "]")
    }
}


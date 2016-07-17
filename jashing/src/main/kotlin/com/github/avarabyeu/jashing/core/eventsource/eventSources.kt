package com.github.avarabyeu.jashing.core.eventsource

import com.github.avarabyeu.jashing.core.JashingEvent
import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.AbstractExecutionThreadService
import com.google.common.util.concurrent.AbstractScheduledService
import com.google.inject.Inject
import org.slf4j.LoggerFactory
import org.slf4j.helpers.MessageFormatter
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Scheduled EventSource - produces events with specified time period.
 * Basically, based on Guava's [com.google.common.util.concurrent.AbstractScheduledService]

 * @author avarabyeu
 */
abstract class ScheduledEventSource<T : JashingEvent> : AbstractScheduledService() {

    val LOGGER = LoggerFactory.getLogger(ScheduledEventSource::class.java)


    /**
     * Time period between sending events
     */
    @Frequency
    @Inject
    lateinit var period: Duration

    /**
     * ID of event this event source bound to
     */
    @EventId
    @Inject
    lateinit var eventId: String

    /**
     * EventBus to send events
     */
    @Inject
    lateinit var eventBus: EventBus


    /**
     * Executes one iteration of [com.google.common.util.concurrent.AbstractScheduledService]
     * Sends event to event bus

     * @throws Exception
     */
    @Throws(Exception::class)
    override fun runOneIteration() {
        try {
            sendEvent(produceEvent())
        } catch (e: Exception) {
            LOGGER.error(MessageFormatter.format("Cannot produce event with id {}", eventId).message, e)
        }

    }


    protected fun sendEvent(t: T?) {
        if (null != t) {
            t.id = eventId
            this.eventBus.post(t)
        }
    }

    override fun scheduler(): AbstractScheduledService.Scheduler {
        return AbstractScheduledService.Scheduler.newFixedDelaySchedule(3, period.toMillis(), TimeUnit.MILLISECONDS)
    }

    override fun serviceName(): String {
        return "ScheduledEventSource[eventID=$eventId]"
    }

    protected abstract fun produceEvent(): T


}

internal abstract class SimpleEventSource<T : JashingEvent> : AbstractExecutionThreadService() {


    /**
     * EventBus to send events
     */
    @javax.inject.Inject
    private val eventBus: EventBus? = null

    /**
     * ID of event this event source bound to
     */
    @EventId
    @javax.inject.Inject
    private val eventId: String? = null

    protected fun sendEvent(t: T?) {
        if (null != t) {
            t.id = eventId!!
            this.eventBus!!.post(t)
        }
    }

    @Throws(Exception::class)
    override fun startUp() {
        /* no any lifecycle-related logic */
    }

    @Throws(Exception::class)
    override fun shutDown() {
        /* no any lifecycle-related logic */
    }

    protected abstract fun produceEvent(): T

    override fun serviceName(): String {
        return "SimpleEventSource[eventID=$eventId]"
    }
}


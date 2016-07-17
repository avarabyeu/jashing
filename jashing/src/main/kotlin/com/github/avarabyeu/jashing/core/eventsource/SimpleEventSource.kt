package com.github.avarabyeu.jashing.core.eventsource

import com.github.avarabyeu.jashing.core.JashingEvent
import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId
import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.AbstractExecutionThreadService
import javax.inject.Inject

/**
 * @author Andrei Varabyeu
 */
internal abstract class SimpleEventSource<T : JashingEvent> : AbstractExecutionThreadService() {


    /**
     * EventBus to send events
     */
    @Inject
    private val eventBus: EventBus? = null

    /**
     * ID of event this event source bound to
     */
    @EventId
    @Inject
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

package com.github.avarabyeu.jashing.core

import com.github.avarabyeu.jashing.core.eventsource.EventId
import java.time.Instant

/**
 * Base Event for all Jashing events

 * @author avarabyeu
 */
open class JashingEvent {
    @EventId lateinit var id: String
    val updatedAt: Long = Instant.now().epochSecond

    /* Do not populate if you don't need dynamic title */
    var title: String? = null

}

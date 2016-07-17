package com.github.avarabyeu.jashing.core

import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId
import java.time.Instant

/**
 * Base Event for all Jashing events

 * @author avarabyeu
 */
open class JashingEvent {
    @EventId lateinit var id: String
    val updatedAt: Long

    /* Do not populate if you don't need dynamic title */
    var title: String? = null

    init {
        this.updatedAt = Instant.now().epochSecond
    }
}

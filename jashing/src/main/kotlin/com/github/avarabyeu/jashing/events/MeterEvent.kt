package com.github.avarabyeu.jashing.events

import com.github.avarabyeu.jashing.core.JashingEvent

/**
 * Meter Event

 * @author Andrei Varabyeu
 */
class MeterEvent : JashingEvent {

    val value: Byte

    constructor(value: Byte) {
        this.value = value
    }

    constructor(title: String, value: Byte) {
        this.value = value
        this.title = title
    }
}

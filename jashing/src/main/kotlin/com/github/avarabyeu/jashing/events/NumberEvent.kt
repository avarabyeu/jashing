package com.github.avarabyeu.jashing.events

import com.github.avarabyeu.jashing.core.JashingEvent

/**
 * Number Event
 * Contains current and last values

 * @author Andrei Varabyeu
 */
class NumberEvent(val current: Int, val last: Int) : JashingEvent()

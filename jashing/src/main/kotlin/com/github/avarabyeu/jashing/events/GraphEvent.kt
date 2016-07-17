package com.github.avarabyeu.jashing.events

import com.github.avarabyeu.jashing.core.JashingEvent

/**
 * @author Andrei Varabyeu
 */
class GraphEvent(val points: Collection<GraphEvent.Point>) : JashingEvent() {

    class Point(val x: Int, val y: Int)
}

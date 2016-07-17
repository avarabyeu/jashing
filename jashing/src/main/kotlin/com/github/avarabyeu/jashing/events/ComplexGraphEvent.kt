package com.github.avarabyeu.jashing.events

import com.github.avarabyeu.jashing.core.JashingEvent

/**
 * Complex Graph event for RickshawGraph widget
 * @author Andrei Varabyeu
 */
class ComplexGraphEvent : JashingEvent {

    val series: List<Series>

    var displayedValue: String? = null

    constructor(series: List<Series>) {
        this.series = series
    }

    constructor(series: List<Series>, displayedValue: String) {
        this.series = series
        this.displayedValue = displayedValue
    }


    class Series(val name: String, val data: List<Point>)


    class Point(var x: Long?, var y: Long?)
}

package com.github.avarabyeu.jashing.events


import com.github.avarabyeu.jashing.core.JashingEvent
import com.google.gson.annotations.SerializedName


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

/**
 * @author Andrei Varabyeu
 */
class GraphEvent(val points: Collection<GraphEvent.Point>) : JashingEvent() {

    class Point(val x: Int, val y: Int)
}

/**
 * @author Andrei Varabyeu
 */
class ListEvent<T>(val items: List<ListEvent.Item<T>>) : JashingEvent() {

    class Item<T>(var label: String?, val value: T)
}

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

/**
 * Number Event
 * Contains current and last values

 * @author Andrei Varabyeu
 */
class NumberEvent(val current: Int, val last: Int) : JashingEvent()

/**
 * @author Andrei Varabyeu
 */
class ProgressBarEvent(title: String?,
                       @SerializedName("progress_items")
                       var items: Collection<ProgressBarEvent.Item>?) : JashingEvent() {
    init {
        this.title = title
    }

    constructor(items: Collection<Item>) : this(null, items) {
    }

    class Item(var name: String?, var progress: Byte)

}

/**
 * @author Text Event
 */
class TextEvent(val text: String) : JashingEvent()

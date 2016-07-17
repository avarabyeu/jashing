package com.github.avarabyeu.jashing.events

import com.github.avarabyeu.jashing.core.JashingEvent
import com.google.gson.annotations.SerializedName

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

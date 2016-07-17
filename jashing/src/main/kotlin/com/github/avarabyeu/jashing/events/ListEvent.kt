package com.github.avarabyeu.jashing.events

import com.github.avarabyeu.jashing.core.JashingEvent

/**
 * @author Andrei Varabyeu
 */
class ListEvent<T>(val items: List<ListEvent.Item<T>>) : JashingEvent() {

    class Item<T>(var label: String?, var value: T?)
}

package com.github.avarabyeu.jashing.core

/**
 * Events and event sources configuration

 * @author avarabyeu
 */
class Configuration {

    /**
     * application-scope properties, might be injected into event source beans
     */
    var properties: Map<String, String>? = emptyMap()

    /**
     * events configuration
     */
    var events: List<EventConfig>? = null


    class EventConfig {
        var id: String? = null
        var source: String? = null
        var frequency: Long = 0

        /**
         * Event-scope properties. Might be injected into event source bean related to this particular event
         */
        var properties: Map<String, Any>? = null
    }


}

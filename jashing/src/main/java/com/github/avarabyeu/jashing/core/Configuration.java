package com.github.avarabyeu.jashing.core;

import java.util.List;
import java.util.Map;

/**
 * Events and event sources configuration
 *
 * @author avarabyeu
 */
class Configuration {

    /**
     * application-scope properties, might be injected into event source beans
     */
    private Map<String, String> properties;

    /**
     * events configuration
     */
    private List<EventConfig> events;

    public List<EventConfig> getEvents() {
        return events;
    }

    public void setEvents(List<EventConfig> events) {
        this.events = events;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public static class EventConfig {
        private String id;
        private String type;
        private long frequency;

        /**
         * Event-scope properties. Might be injected into event source bean related to this particular event
         */
        private Map<String, ?> properties;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getFrequency() {
            return frequency;
        }

        public void setFrequency(long frequency) {
            this.frequency = frequency;
        }

        public Map<String, ?> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, ?> properties) {
            this.properties = properties;
        }
    }


}

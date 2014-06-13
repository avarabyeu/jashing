package com.github.avarabyeu.jashing;

import java.util.List;
import java.util.Map;

/**
 * Application configuration. Used for configuring event sources
 *
 * @author avarabyeu
 */
public class Configuration {

    private List<EventConfig> events;

    public List<EventConfig> getEvents() {
        return events;
    }

    public void setEvents(List<EventConfig> events) {
        this.events = events;
    }

    public static class EventConfig {
        private String id;
        private String type;
        private long frequency;
        private Map<String, ? extends Object> properties;


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

        public Map<String, ? extends Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, ? extends Object> properties) {
            this.properties = properties;
        }
    }


}

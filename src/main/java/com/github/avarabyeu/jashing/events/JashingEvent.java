package com.github.avarabyeu.jashing.events;

import java.time.Instant;

/**
 * Base Event for all Jashing events
 *
 * @author avarabyeu
 */
public class JashingEvent {
    private String id;
    private long updatedAt;

    public JashingEvent() {
        this.updatedAt = Instant.now().toEpochMilli();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}

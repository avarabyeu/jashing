package com.github.avarabyeu.jashing.core;

import java.time.Instant;

/**
 * Base Event for all Jashing events
 *
 * @author avarabyeu
 */
public class JashingEvent {
    private String id;
    private long updatedAt;

    /* Do not populate if you don't need dynamic title */
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

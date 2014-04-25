package com.github.avarabyeu.jashing.events;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrey.vorobyov on 24/04/14.
 */
public class JashingEvent {
    private String id;
    private long updatedAt;

    public JashingEvent(String id) {
        this.id = id;
        this.updatedAt = TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis());
    }

    public String getId() {
        return id;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}

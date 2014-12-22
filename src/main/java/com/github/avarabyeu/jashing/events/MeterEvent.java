package com.github.avarabyeu.jashing.events;

import com.github.avarabyeu.jashing.core.JashingEvent;

/**
 * Meter Event
 *
 * @author Andrei Varabyeu
 */
public class MeterEvent extends JashingEvent {

    private byte value;

    public MeterEvent(byte value) {
        this.value = value;
    }

    public MeterEvent(String title, byte value) {
        this.value = value;
        setTitle(title);
    }

    public byte getValue() {
        return value;
    }
}

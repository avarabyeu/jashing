package com.github.avarabyeu.jashing.events;

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

    public byte getValue() {
        return value;
    }
}

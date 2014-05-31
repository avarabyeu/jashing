package com.github.avarabyeu.jashing.events;

/**
 * Created by andrey.vorobyov on 24/04/14.
 */
public class MeterEvent extends JashingEvent {

    private byte value;

    public MeterEvent(byte value) {
        super("synergy");
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}

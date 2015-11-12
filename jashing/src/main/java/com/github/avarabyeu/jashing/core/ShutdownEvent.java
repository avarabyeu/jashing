package com.github.avarabyeu.jashing.core;

/**
 * Should be sent on system shutdown (doesn't matter, expected or not)
 *
 * @author avarabyeu
 */
public class ShutdownEvent {

    public static final ShutdownEvent INSTANCE = new ShutdownEvent();

    private ShutdownEvent() {
        //default constructor
    }

}

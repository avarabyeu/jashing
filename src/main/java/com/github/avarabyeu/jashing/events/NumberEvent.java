package com.github.avarabyeu.jashing.events;

import com.github.avarabyeu.jashing.core.JashingEvent;

/**
 * Number Event <br>
 * Contains current and last values
 *
 * @author Andrei Varabyeu
 */
public class NumberEvent extends JashingEvent {

    private int current;
    private int last;

    public NumberEvent(int current, int last) {
        this.current = current;
        this.last = last;
    }

    public int getCurrent() {
        return current;
    }

    public int getLast() {
        return last;
    }
}

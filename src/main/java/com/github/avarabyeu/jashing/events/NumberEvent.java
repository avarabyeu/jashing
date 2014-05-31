package com.github.avarabyeu.jashing.events;

/**
 * Created by andrey.vorobyov on 31/05/14.
 */
public class NumberEvent extends JashingEvent {

    private int current;
    private int last;

    public NumberEvent(int current, int last) {
        super("valuation");
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

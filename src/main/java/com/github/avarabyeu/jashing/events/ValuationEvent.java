package com.github.avarabyeu.jashing.events;

/**
 * Created by andrey.vorobyov on 30/04/14.
 */
public class ValuationEvent extends JashingEvent {

    private int current;
    private int last;

    public ValuationEvent(int current, int last) {
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

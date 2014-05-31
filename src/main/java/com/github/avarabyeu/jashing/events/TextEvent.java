package com.github.avarabyeu.jashing.events;

/**
 * Created by andrey.vorobyov on 31/05/14.
 */
public class TextEvent extends JashingEvent {

    private final String text;

    public TextEvent(String text) {
        super("welcome");
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

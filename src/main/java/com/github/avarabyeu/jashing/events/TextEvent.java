package com.github.avarabyeu.jashing.events;

/**
 * @author Text Event
 */
public class TextEvent extends JashingEvent {

    private final String text;

    public TextEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

package com.github.avarabyeu.jashing.events;

import com.github.avarabyeu.jashing.core.JashingEvent;

import java.util.List;

/**
 * @author Andrey Vorobyov
 */
public class ListEvent<T> extends JashingEvent {

    private List<Item<T>> items;

    public ListEvent(List<Item<T>> items) {
        this.items = items;
    }

    public List<Item<T>> getItems() {
        return items;
    }

    public static class Item<T> {
        private String label;
        private T value;

        public Item() {
        }

        public Item(String label, T value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}

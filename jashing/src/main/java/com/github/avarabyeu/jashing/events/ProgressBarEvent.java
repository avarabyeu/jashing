package com.github.avarabyeu.jashing.events;

import com.github.avarabyeu.jashing.core.JashingEvent;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * @author Andrei Varabyeu
 */
public class ProgressBarEvent extends JashingEvent {

    @SerializedName("progress_items")
    private Collection<Item> items;

    public ProgressBarEvent(String title, Collection<Item> items) {
        setTitle(title);
        this.items = items;
    }

    public ProgressBarEvent(Collection<Item> items) {
        this(null, items);
    }


    public Collection<Item> getItems() {
        return items;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String name;
        private byte progress;

        public Item(String name, byte progress) {
            this.name = name;
            this.progress = progress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public byte getProgress() {
            return progress;
        }

        public void setProgress(byte progress) {
            this.progress = progress;
        }
    }

}

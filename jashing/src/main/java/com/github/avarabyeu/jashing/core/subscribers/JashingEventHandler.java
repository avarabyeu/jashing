package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.JashingEvent;
import com.github.avarabyeu.jashing.core.ServerSentEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Register yourself as JashingEvent handler to receive Jashing events and sends them as Server Sent Events
 */
public class JashingEventHandler {

    private EventBus evenBus;

    public JashingEventHandler(EventBus eventBus) {
        this.evenBus = eventBus;
    }

    /**
     * Obtains event from Guava's {@link com.google.common.eventbus.EventBus}, serializes it to string and send
     * in 'Server Sent Events' format
     *
     * @param event Event from Guava's {@link com.google.common.eventbus.EventBus}
     * @see <a href="http://en.wikipedia.org/wiki/Server-sent_events">Server Sent Events</a>
     */
    @Subscribe
    public void dispatch(JashingEvent event) {
        evenBus.post(new ServerSentEvent<>(null, event));
    }
}

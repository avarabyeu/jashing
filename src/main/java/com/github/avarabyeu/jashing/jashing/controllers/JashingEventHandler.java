package com.github.avarabyeu.jashing.jashing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.avarabyeu.jashing.jashing.events.JashingEvent;
import com.github.avarabyeu.jashing.jashing.subscribers.ServerSentEventHandler;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * Register yourself as JashingEvent handler to receive Jashing events and sends them as Server Sent Events
 */
public class JashingEventHandler extends ServerSentEventHandler<JashingEvent> {

    @Inject
    public JashingEventHandler(EventBus eventBus, ObjectMapper serializer) {
        super(eventBus, serializer);
    }

    /**
     * Obtains event from Guava's {@link com.google.common.eventbus.EventBus}, serializes it to string and send
     * in 'Server Sent Events' format
     *
     * @param event Event from Guava's {@link com.google.common.eventbus.EventBus}
     * @see <a href="http://en.wikipedia.org/wiki/Server-sent_events">Server Sent Events</a>
     */
    @Override
    public void onEvent(JashingEvent event) {
        writeEvent(new ServerSentEvent(null, event));
    }
}

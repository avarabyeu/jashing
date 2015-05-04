package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.JashingEvent;
import com.github.avarabyeu.jashing.core.ServerSentEvent;
import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.inject.Inject;

import javax.inject.Named;

/**
 * Register yourself as JashingEvent handler to receive Jashing events and sends them as Server Sent Events
 */
public class JashingEventHandler extends ServerSentEventHandler<JashingEvent> {

    @Inject
    public JashingEventHandler(EventBus eventBus, Gson serializer, @Timeout Optional<Long> timeout) {
        super(eventBus, serializer, timeout);
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
        writeEvent(new ServerSentEvent<>(null, event));
    }
}

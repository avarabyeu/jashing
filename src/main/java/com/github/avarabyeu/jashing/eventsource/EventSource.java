package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.events.JashingEvent;
import com.google.common.util.concurrent.Service;

/**
 * EventSource for {@link com.github.avarabyeu.jashing.events.JashingEvent}. Takes care about sending events
 *
 * @author avarabyeu
 */
interface EventSource<T extends JashingEvent> extends Service {
    void sendEvent(T t);
}

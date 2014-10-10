package com.github.avarabyeu.jashing.core.eventsource;

import com.github.avarabyeu.jashing.core.JashingEvent;
import com.google.common.util.concurrent.Service;

/**
 * //TODO do this abstraction really needed?
 * EventSource for {@link com.github.avarabyeu.jashing.core.JashingEvent}. Takes care about sending events
 *
 * @author avarabyeu
 */
public interface EventSource<T extends JashingEvent> extends Service {
    void sendEvent(T t);
}

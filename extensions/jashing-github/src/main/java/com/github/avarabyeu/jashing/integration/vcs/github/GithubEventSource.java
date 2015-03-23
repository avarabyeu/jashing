package com.github.avarabyeu.jashing.integration.vcs.github;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.MeterEvent;
import com.google.inject.ProvidedBy;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = Events.METER, explicitConfiguration = GithubModule.class)
public class GithubEventSource extends ScheduledEventSource<MeterEvent> {

    @Override
    protected MeterEvent produceEvent() {
        return null;
    }
}

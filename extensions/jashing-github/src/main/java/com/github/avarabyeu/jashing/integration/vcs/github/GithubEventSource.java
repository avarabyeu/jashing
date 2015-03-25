package com.github.avarabyeu.jashing.integration.vcs.github;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.MeterEvent;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = "github-event-source", explicitConfiguration = GithubModule.class)
public class GithubEventSource extends ScheduledEventSource<MeterEvent> {

    @Override
    protected MeterEvent produceEvent() {
        return null;
    }
}

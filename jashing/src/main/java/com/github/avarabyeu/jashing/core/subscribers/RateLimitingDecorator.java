package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.ServerSentEvent;
import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * TODO this is not decorator actually. Consider refactoring to follow pattern rules
 *
 * @author Andrei Varabyeu
 */
public class RateLimitingDecorator extends JashingEventHandler {

    private final Optional<RateLimiter> rateLimiter;

    @Inject
    public RateLimitingDecorator(EventBus eventBus, Gson serializer, @Timeout Optional<Long> timeout) {
        super(eventBus, serializer);
        this.rateLimiter = timeout.transform(tmt -> RateLimiter.create(1.0f / tmt));
    }


    @Override
    protected synchronized void writeEvent(ServerSentEvent event) {
        /* apply per-connection timeout if present */
        if (rateLimiter.isPresent()) {
            rateLimiter.get().acquire();
        }
        super.writeEvent(event);
    }


}

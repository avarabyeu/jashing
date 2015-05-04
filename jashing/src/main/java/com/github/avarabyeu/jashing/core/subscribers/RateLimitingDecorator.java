package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.ServerSentEvent;
import com.github.avarabyeu.jashing.core.ShutdownEvent;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.RateLimiter;

import javax.servlet.AsyncContext;
import java.io.IOException;

/**
 * Decorates {@link ServerSentEventsHandler}. Adds rate limit
 *
 * @author Andrei Varabyeu
 */
public class RateLimitingDecorator implements ServerSentEventsHandler {
    private Optional<RateLimiter> rateLimiter;
    private ServerSentEventsHandler delegate;

    public RateLimitingDecorator(ServerSentEventsHandler delegate, Optional<Long> timeout) {
        this.rateLimiter = timeout.transform(tmt -> RateLimiter.create(1.0f / tmt));
        this.delegate = delegate;
    }

    @Override
    public void handle(AsyncContext asyncContext) throws IOException {
        delegate.handle(asyncContext);
    }

    @Subscribe
    public void onEvent(ServerSentEvent event) throws IOException {
        /* apply per-connection timeout if present */
        if (rateLimiter.isPresent()) {
            rateLimiter.get().acquire();
        }

        delegate.onEvent(event);
    }

    @Override
    public void onShutdown(ShutdownEvent shutdownEvent) {
        delegate.onShutdown(shutdownEvent);
    }
}

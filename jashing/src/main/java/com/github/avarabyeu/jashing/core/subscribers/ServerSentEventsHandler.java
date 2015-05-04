package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.ServerSentEvent;
import com.github.avarabyeu.jashing.core.ShutdownEvent;
import com.google.common.eventbus.Subscribe;

import javax.servlet.AsyncContext;
import java.io.IOException;

/**
 * Represents SSE handler.
 * Subscriber to Guava's {@link com.google.common.eventbus.EventBus}
 * Forces to handle {@link #onShutdown(com.github.avarabyeu.jashing.core.ShutdownEvent)} event to
 * close opened resources. Interface method already have Guava's annotations so subclasses should not duplicate them
 *
 * @author avarabyeu
 * @see <a href="https://code.google.com/p/guava-libraries/wiki/EventBusExplained">Guava's EventBus</a>
 */
public interface ServerSentEventsHandler {


    void handle(AsyncContext asyncContext) throws IOException;

    @Subscribe
    void onEvent(ServerSentEvent t) throws IOException;

    @Subscribe
    void onShutdown(ShutdownEvent shutdownEvent);

}

package com.github.avarabyeu.jashing.jashing.subscribers;

import com.github.avarabyeu.jashing.jashing.events.ShutdownEvent;
import com.google.common.eventbus.Subscribe;

/**
 * Represents subscriber to Guava's {@link com.google.common.eventbus.EventBus}
 * Forces to handle {@link #onShutdown(com.github.avarabyeu.jashing.jashing.events.ShutdownEvent)} event to
 * close opened resources. Interface method already have Guava's annotations so subclasses should not duplicate them
 *
 * @author avarabyeu
 * @see <a href="https://code.google.com/p/guava-libraries/wiki/EventBusExplained">Guava's EventBus</a>
 */
public interface EventSubscriber<T> {


    @Subscribe
    void onEvent(T t);

    @Subscribe
    void onShutdown(ShutdownEvent shutdownEvent);

}

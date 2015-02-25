package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.ShutdownEvent;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

/**
 * Abstract implementation of {@link EventSubscriber}
 * with possibility to subscribe and unsubscribe yourself from {@link com.google.common.eventbus.EventBus}
 * Automatically unsubscribes yourself from {@link com.google.common.eventbus.EventBus} on system shutdown
 *
 * @author avarabyeu
 */
public abstract class IndependentSubscriber<T> implements EventSubscriber<T> {

    /**
     * EventBus subscriber should be registered to
     */
    private final EventBus eventBus;

    protected IndependentSubscriber(EventBus eventBus) {
        this.eventBus = Preconditions.checkNotNull(eventBus, "EventBus shouldn't be null");
    }

    /**
     * Subscribes yourself to {@link com.google.common.eventbus.EventBus}
     */
    public void subscribe() {
        this.eventBus.register(this);
    }

    /**
     * Unsubscribes yourself to {@link com.google.common.eventbus.EventBus}
     */
    public void unsubscribe() {
        this.eventBus.unregister(this);
    }

    /**
     * Automatically unsubscribes yourself in case of system shutdown
     *
     * @param shutdownEvent ShutdownEvent
     */
    @Override
    public void onShutdown(ShutdownEvent shutdownEvent) {
        unsubscribe();
    }
}

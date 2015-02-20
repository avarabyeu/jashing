package com.github.avarabyeu.jashing.core.subscribers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Guava's Logging Subscriber to be connected with slf4j logger
 *
 * @author avarabyeu
 */
public class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler {

    /**
     * Logger for event dispatch failures.  Named by the fully-qualified name of
     * this class, followed by the identifier provided at construction.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class.getName());


    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        LOGGER.error("Could not dispatch event: {} to {}",
                context.getSubscriber(), context.getSubscriberMethod(),
                exception.getCause()
        );
    }
}

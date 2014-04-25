package com.github.avarabyeu.jashing;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Guava's Logging Subscriber to be connected with slf4j logger
 * @author avarabyeu
 */
public class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler {

    /**
     * Logger for event dispatch failures.  Named by the fully-qualified name of
     * this class, followed by the identifier provided at construction.
     */
    private final Logger logger;

    /**
     * @param identifier a brief name for this bus, for logging purposes. Should
     *                   be a valid Java identifier.
     */
    public LoggingSubscriberExceptionHandler(String identifier) {
        logger = LoggerFactory.getLogger(
                EventBus.class.getName() + "." + checkNotNull(identifier));
    }

    @Override
    public void handleException(Throwable exception,
                                SubscriberExceptionContext context) {
        logger.error("Could not dispatch event: {} to {}"
                        + context.getSubscriber(), context.getSubscriberMethod(),
                exception.getCause()
        );
    }
}

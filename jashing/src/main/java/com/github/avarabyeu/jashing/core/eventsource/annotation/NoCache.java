package com.github.avarabyeu.jashing.core.eventsource.annotation;

import java.lang.annotation.*;

/**
 * Defines an event for load up cache eviction
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoCache {
}

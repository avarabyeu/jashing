package com.github.avarabyeu.jashing.core;

import com.google.inject.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks class as event source for specified event type
 *
 * @author Andrei Varabyeu
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface HandlesEvent {

    interface NOP extends Module {}

    /**
     * Event type name
     */
    String value();

    Class<? extends Module> explicitConfiguration() default NOP.class;
}

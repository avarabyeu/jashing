package com.github.avarabyeu.jashing.eventsource.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.*;

/**
 * Binds event ID to field this annotation specified on
 *
 * @author Andrey Vorobyov
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@BindingAnnotation
public @interface EventId {
}

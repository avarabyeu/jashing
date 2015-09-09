package com.github.avarabyeu.jashing.core.eventsource.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binds event frequency duration to field this annotation specified on
 *
 * @author Andrey Vorobyov
 */
@Target(value = { ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@BindingAnnotation
public @interface Frequency {
}

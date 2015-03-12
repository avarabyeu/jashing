package com.github.avarabyeu.jashing.core;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Guice Binding Annotation for Jashing global properties
 * May be applied to {@code Map<String,String>}
 *
 * @author Andrei Varabyeu
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@BindingAnnotation
public @interface GlobalProperties {
}

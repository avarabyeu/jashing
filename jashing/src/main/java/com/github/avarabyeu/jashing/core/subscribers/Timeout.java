package com.github.avarabyeu.jashing.core.subscribers;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.*;

/**
 * Binds per-client timeout property
 *
 * @author Andrei Varabyeu
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@BindingAnnotation
public @interface Timeout {
}

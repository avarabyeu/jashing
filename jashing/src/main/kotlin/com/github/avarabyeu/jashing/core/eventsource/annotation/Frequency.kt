package com.github.avarabyeu.jashing.core.eventsource.annotation

import com.google.inject.BindingAnnotation


/**
 * Binds event frequency duration to field this annotation specified on

 * @author Andrei Varabyeu
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@BindingAnnotation
annotation class Frequency

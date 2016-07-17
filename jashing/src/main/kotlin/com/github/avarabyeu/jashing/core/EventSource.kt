package com.github.avarabyeu.jashing.core

import com.google.inject.BindingAnnotation
import com.google.inject.Module
import kotlin.reflect.KClass

/**
 * Marks class as event source for specified event type

 * @author Andrei Varabyeu
 */


@kotlin.annotation.Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@BindingAnnotation
annotation class EventSource(

        /**
         * Event source name
         */
        val value: String = "",
        val explicitConfiguration: KClass<out Module> = EventsModule.Companion.NOP::class


)

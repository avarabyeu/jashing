package com.github.avarabyeu.jashing.utils

import com.google.inject.Binder
import com.google.inject.Key
import com.google.inject.multibindings.OptionalBinder
import com.google.inject.name.Names
import javax.inject.Provider

/**
 * @author Andrei Varabyeu
 */
object GuiceUtils {

    /**
     * Obtains property [Key]

     * @param propertyName Name of Property
     * *
     * @return property key
     */
    fun getPropertyKey(propertyName: String): Key<String> {
        return Key.get(String::class.java, Names.named(propertyName))
    }

    /**
     * Obtains property provider

     * @param binder       Guice's binder
     * *
     * @param propertyName Name of Property
     * *
     * @return Property Provider
     */
    fun getPropertyProvider(binder: Binder, propertyName: String): Provider<String> {
        return binder.getProvider(getPropertyKey(propertyName))
    }

    /**
     * Binds default value to specified property

     * @param binder       Guice's Binder
     * *
     * @param propertyName Name of property
     * *
     * @param defaultValue Default value
     * *
     * @return Provider to new bound value
     */
    fun bindDefault(binder: Binder, propertyName: String, defaultValue: String): Provider<String> {
        val propertyKey = getPropertyKey(propertyName)
        val optionalBinder = OptionalBinder.newOptionalBinder(binder, propertyKey)
        optionalBinder.setDefault().toInstance(defaultValue)
        return binder.getProvider(propertyKey)
    }
}

package com.github.avarabyeu.jashing.utils;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.name.Names;

import javax.inject.Provider;

/**
 * @author Andrei Varabyeu
 */
public final class GuiceUtils {


    private GuiceUtils() {
    }

    /**
     * Obtains property {@link Key}
     *
     * @param propertyName Name of Property
     * @return property key
     */
    public static Key<String> getPropertyKey(String propertyName) {
        return Key.get(String.class, Names.named(propertyName));
    }

    /**
     * Obtains property provider
     *
     * @param binder       Guice's binder
     * @param propertyName Name of Property
     * @return Property Provider
     */
    public static Provider<String> getPropertyProvider(Binder binder, String propertyName) {
        return binder.getProvider(getPropertyKey(propertyName));
    }

    /**
     * Binds default value to specified property
     *
     * @param binder       Guice's Binder
     * @param propertyName Name of property
     * @param defaultValue Default value
     * @return Provider to new bound value
     */
    public static Provider<String> bindDefault(Binder binder, String propertyName, String defaultValue) {
        Key<String> propertyKey = getPropertyKey(propertyName);
        OptionalBinder<String> optionalBinder = OptionalBinder.
                newOptionalBinder(binder, propertyKey);
        optionalBinder.setDefault().toInstance(defaultValue);
        return binder.getProvider(propertyKey);
    }
}

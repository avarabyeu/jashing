package com.github.avarabyeu.jashing.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains object as values and it's classes as keys
 * Extends default {@code {java.util.Map<Class<? extends T>, T>}} map with {@link #getInstanceOf(Class)} method
 * which first calls {@link #get(Object)} (and returns value if found) and after goes through entries and
 * tries to find values using {@link java.lang.Class#isAssignableFrom(Class)} construction
 *
 * @author Andrei Varabyeu
 */
public final class InstanceOfMap<T> extends ForwardingMap<Class<? extends T>, T> {
    private Map<Class<? extends T>, T> delegate;

    private InstanceOfMap(Map<Class<? extends T>, T> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Map<Class<? extends T>, T> delegate() {
        return delegate;
    }

    @Override
    public T put(@Nullable Class<? extends T> clazz, @Nullable T value) {
        return delegate.put(clazz, value);
    }

    public T getInstanceOf(Class<? extends T> clazz) {
        if (delegate.containsKey(clazz)) {
            return delegate.get(clazz);
        } else {
            return delegate.entrySet().stream()
                    .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst().orElse(null);
        }
    }

    public Collection<T> values() {
        return delegate.values();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> InstanceOfMap<T> empty() {
        return new InstanceOfMap<>(Collections.emptyMap());
    }

    public static class Builder<T> {

        @SuppressWarnings("unchecked")
        public InstanceOfMap<T> fromList(List<? extends T> list) {
            Preconditions.checkNotNull(list, "Provided list is null!");
            Map<Class<? extends T>, T> map = new HashMap<>(list.size());
            for (T item : list) {
                map.put((Class<? extends T>) item.getClass(), item);
            }
            return new InstanceOfMap<>(map);
        }

        public InstanceOfMap<T> fromArray(T... array) {
            Preconditions.checkNotNull(array, "Provided array is null!");
            return fromList(Arrays.asList(array));
        }

        public InstanceOfMap<T> fromMap(Map<Class<? extends T>, T> map) {
            return new InstanceOfMap<>(map);
        }

    }

}

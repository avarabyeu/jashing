package com.github.avarabyeu.jashing.utils

import com.google.common.base.Preconditions
import com.google.common.collect.ForwardingMap
import java.util.*

/**
 * Contains object as values and it's classes as keys
 * Extends default `{java.util.Map&lt;Class&lt;? extends T&gt;, T&gt;}` map with [.getInstanceOf] method
 * which first calls [.get] (and returns value if found) and after goes through entries and
 * tries to find values using [java.lang.Class.isAssignableFrom] construction

 * @author Andrei Varabyeu
 */
class InstanceOfMap<T>(val delegate: MutableMap<Class<out T>, T>) : ForwardingMap<Class<out T>, T>() {

    override fun delegate(): Map<Class<out T>, T> {
        return delegate
    }

    override fun put(key: Class<out T>, value: T): T? {
        return delegate.put(key, value)
    }

    fun getInstanceOf(clazz: Class<out T>): T? {
        if (delegate.containsKey(clazz)) {
            return delegate[clazz]
        } else {
            return delegate.entries.filter({ entry -> clazz.isAssignableFrom(entry.key) }).map({ it.value }).firstOrNull();
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null || javaClass != other.javaClass)
            return false
        if (!super.equals(other))
            return false
        val that = other as InstanceOfMap<*>?
        return delegate == that!!.delegate
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), delegate)
    }


    class Builder<T> where T : Any {

        fun fromList(list: List<T>): InstanceOfMap<T> {
            Preconditions.checkNotNull(list, "Provided list is null!")
            val map = HashMap<Class<out T>, T>(list.size)
            for (item in list) {

                map.put(item.javaClass as Class<out T>, item)
            }
            return InstanceOfMap(map)
        }

        fun fromArray(array: T): InstanceOfMap<T> {
            Preconditions.checkNotNull(array, "Provided array is null!")
            return fromList(Arrays.asList(array) as List<T>)
        }

        fun fromMap(map: MutableMap<Class<out T>, T>): InstanceOfMap<T> {
            return InstanceOfMap(map)
        }

    }


    companion object {

        @JvmStatic
        fun <T> builder(): Builder<T> where T : Any {
            return Builder()
        }

        @JvmStatic
        fun <T> empty(): InstanceOfMap<T> {
            return InstanceOfMap(mutableMapOf());
        }

    }

}

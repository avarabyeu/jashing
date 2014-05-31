package com.github.avarabyeu.jashing.eventsource;

import com.google.common.eventbus.EventBus;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by andrey.vorobyov on 25/04/14.
 */
public class EventsModule extends AbstractModule {



    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        try {
            List<EventSource> eventSources = new ArrayList<>();
            Set<ClassPath.ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClassesRecursive("com.github.avarabyeu.jashing.eventsource");
            for (ClassPath.ClassInfo classInfo : classes) {
                Class<?> clazz = classInfo.load();
                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) && EventSource.class.isAssignableFrom(clazz)) {
                    EventSource eventSource = (EventSource) TypeToken.of(clazz).constructor(clazz.getConstructor()).invoke(null);
                    binder().requestInjection(eventSource);
                    eventSources.add(eventSource);
                }

            }
            ServiceManager instance = new ServiceManager(eventSources);
            binder().bind(ServiceManager.class).toInstance(instance);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }





}

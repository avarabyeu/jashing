package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.core.eventsource.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.core.eventsource.annotation.Frequency;
import com.google.common.base.Preconditions;
import com.google.common.reflect.ClassPath;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.spi.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Guice configuration {@link com.google.inject.Module} for event handlers. Create all defined in configuration event handlers,
 * populates them with needed values and registers as services
 *
 * @author avarabyeu
 */
class EventsModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsModule.class);

    private static final TypeLiteral<EventSource<?>> EVENT_SOURCE_TYPE = new TypeLiteral<EventSource<?>>() {
    };


    private final List<Configuration.EventConfig> eventConfigs;

    public EventsModule(List<Configuration.EventConfig> eventConfigs) {
        this.eventConfigs = Preconditions.checkNotNull(eventConfigs, "Event configs shouldn't be null");
    }

    @Override
    protected void configure() {
        try {
            Map<String, List<Class<? extends EventSource<?>>>> eventHandlers = mapEventHandlers();

            final Multibinder<EventSource<?>> eventSourceMultibinder = Multibinder.newSetBinder(binder(), EVENT_SOURCE_TYPE);

            for (Configuration.EventConfig event : eventConfigs) {
                if (!eventHandlers.containsKey(event.getType())) {
                    binder().addError("Unable to find handler for event with type '%s'", event.getType());
                } else {
                    List<Class<? extends EventSource<?>>> handlerClasses = eventHandlers.get(event.getType());
                    if (handlerClasses.isEmpty()) {
                        addError("Event Handler for event with type '%s' not found", event.getType());
                    } else {
                        if (handlerClasses.size() > 1) {
                            LOGGER.warn("Event with type '%' bound more than to one event handler. Using first one...", event.getType());
                        }
                        install(new EventSourcePrivateModule(event, eventSourceMultibinder, handlerClasses.get(0)));
                    }
                }
            }

        } catch (IOException e) {
            addError(new Message("Unable to load event handlers...", e));
        }
    }

    @Provides
    @Singleton
    public ServiceManager serviceManager(Set<EventSource<?>> eventSources) {
        return new ServiceManager(eventSources);
    }


    /**
     * Scans whole application classpath and finds events handlers
     *
     * @return 'event name' -> 'list of handlers classes' map
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, List<Class<? extends EventSource<?>>>> mapEventHandlers() throws IOException {

        /** Obtains all classpath's top level classes */
        Set<ClassPath.ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClassesRecursive("com.github.avarabyeu");
        LOGGER.info("Scanning classpath for EventHandlers....");

        /* iterates over all classes, filter by HandlesEvent annotation and transforms stream to needed form */
        Map<String, List<Class<? extends EventSource<?>>>> collected = classes.parallelStream()
                /* loads class infos */
                .map(classInfo -> {
                    try {
                        /* sometimes exception occurs during class loading. Return empty/absent in this case */
                        return Optional.<Class<?>>of(classInfo.load());
                    } catch (Throwable e) {
                        return Optional.<Class<?>>empty();
                    }
                })
                /* filters classes which is present and marked with HandlesEvent annotation */
                .filter(((Predicate<Optional<Class<?>>>) Optional::isPresent)
                        .and(classOptional -> classOptional.get().isAnnotationPresent(HandlesEvent.class))
                        .and(classOptional -> EventSource.class.isAssignableFrom(classOptional.get())))
                /* transforms from Optional<Class> to Class (obtaining values from Optionals) */
                .map(optional -> (Class<? extends EventSource<?>>) optional.get())
                .collect(Collectors.groupingBy(clazz -> clazz.getAnnotation(HandlesEvent.class).value(), Collectors.toList()));

        LOGGER.info("Found {} event handlers", collected.size());
        return collected;
    }


    /**
     * {@link com.google.inject.PrivateModule} binds event source type to some particular event and exposes it to parent module
     */
    private static class EventSourcePrivateModule extends PrivateModule {

        private Configuration.EventConfig event;

        private Multibinder<EventSource<?>> multibinder;

        private Class<? extends EventSource<?>> handlerClass;

        public EventSourcePrivateModule(Configuration.EventConfig event, Multibinder<EventSource<?>> multibinder, Class<? extends EventSource<?>> handlerClass) {
            this.event = event;
            this.multibinder = multibinder;
            this.handlerClass = handlerClass;
        }

        @Override
        protected void configure() {
            validateEvent();

            binder().bind(Duration.class).annotatedWith(Frequency.class).toInstance(Duration.ofSeconds(event.getFrequency()));
            binder().bind(String.class).annotatedWith(EventId.class).toInstance(event.getId());

            Key<EventSource<?>> eventSourceKey = Key.get(EVENT_SOURCE_TYPE, Names.named(event.getId()));
            binder().bind(eventSourceKey).to(handlerClass);

            expose(eventSourceKey);

            multibinder.addBinding().to(eventSourceKey);

            if (null != event.getProperties()) {
                event.getProperties().entrySet().forEach(entry ->
                        bindProperty(entry.getKey(), entry.getValue()));
            }

        }

        @SuppressWarnings("unchecked")
        private <T> void bindProperty(String key, T value) {
            TypeLiteral<T> type = TypeLiteral.get((Class<T>) value.getClass());
            bind(type).annotatedWith(Names.named(key)).toInstance(value);
        }

        private void validateEvent() {
            if (event.getFrequency() <= 0) {
                binder().addError("Frequency of event with type '%s' is not specified", event.getType());
            }

            if (null == event.getId()) {
                binder().addError("ID of event with type '%s' is not specified", event.getType());
            }
        }
    }


}

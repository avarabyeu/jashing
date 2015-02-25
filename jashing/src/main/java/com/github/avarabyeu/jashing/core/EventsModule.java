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


    private final List<Configuration.EventConfig> eventConfigs;

    @Inject
    public EventsModule(List<Configuration.EventConfig> eventConfigs) {
        this.eventConfigs = Preconditions.checkNotNull(eventConfigs, "Event configs shouldn't be null");
    }

    @Override
    protected void configure() {
        try {
            Map<String, Class<?>> eventHandlers = mapEventHandlers();

            final Multibinder<EventSource> eventSourceMultibinder = Multibinder.newSetBinder(binder(), EventSource.class);

            eventConfigs.stream().forEach(event -> {

                if (!eventHandlers.containsKey(event.getType())) {
                    throw new IncorrectConfigurationException("Unable to find handler for event with type '" + event.getType() + "'");
                }

                install(new PrivateModule() {

                    @SuppressWarnings("unchecked")
                    @Override
                    protected void configure() {
                        Class<? extends EventSource> handlerClass = (Class<? extends EventSource>) eventHandlers.get(event.getType());

                        binder().bind(Duration.class).annotatedWith(Frequency.class).toInstance(Duration.ofSeconds(event.getFrequency()));
                        binder().bind(String.class).annotatedWith(EventId.class).toInstance(event.getId());

                        Key<EventSource> eventSourceKey = Key.get(EventSource.class, Names.named(event.getId()));
                        binder().bind(eventSourceKey).to(handlerClass);

                        expose(eventSourceKey);

                        eventSourceMultibinder.addBinding().to(eventSourceKey);

                        if (null != event.getProperties()) {
                            event.getProperties().entrySet().forEach(entry ->
                                    bindProperty(entry.getKey(), entry.getValue()));
                        }

                    }

                    private <T> void bindProperty(String key, T value) {
                        TypeLiteral<T> type = TypeLiteral.get((Class<T>) value.getClass());
                        bind(type).annotatedWith(Names.named(key)).toInstance(value);
                    }
                });


            });

        } catch (IOException e) {
            throw new IncorrectConfigurationException("Unable to load event handlers...", e);
        }
    }

    @Provides
    @Singleton
    public ServiceManager serviceManager(Set<EventSource> eventSources) {
        return new ServiceManager(eventSources);
    }


    /**
     * Scans whole application classpath and find events handlers
     *
     * @return 'event name' -> 'handler class' map
     * @throws IOException
     */
    public static Map<String, Class<?>> mapEventHandlers() throws IOException {

        /** Obtains all classpath's top level classes */
        Set<ClassPath.ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClassesRecursive("com.github.avarabyeu");
        LOGGER.info("Scanning classpath for EventHandlers....");

        /* iterates over all classes, filter by HandlesEvent annotation and transforms stream to needed form */
        Map<String, Class<?>> collected = classes.parallelStream().map(classInfo -> {
            try {
                return Optional.<Class<?>>of(classInfo.load());
            } catch (Throwable e) {
                return Optional.<Class<?>>empty();
            }
        }).filter(((Predicate<Optional<Class<?>>>) Optional::isPresent).and(classOptional -> classOptional.get().isAnnotationPresent(HandlesEvent.class))).map(Optional::get)
                .collect(Collectors.toMap(clazz -> clazz.getAnnotation(HandlesEvent.class).value(), clazz -> clazz));
        LOGGER.info("Found {} event handlers", collected.size());
        return collected;
    }


}

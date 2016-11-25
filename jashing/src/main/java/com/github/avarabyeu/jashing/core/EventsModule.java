package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.core.eventsource.annotation.Frequency;
import com.github.avarabyeu.jashing.utils.InstanceOfMap;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.reflect.ClassPath;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Exposed;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.name.Names;
import com.google.inject.spi.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
class EventsModule extends PrivateModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsModule.class);


    private final List<Configuration.EventConfig> eventConfigs;

    private final InstanceOfMap<Module> extensionsMap;

    public EventsModule(@Nonnull List<Configuration.EventConfig> eventConfigs) {
        this(eventConfigs, null);
    }

    public EventsModule(@Nonnull List<Configuration.EventConfig> eventConfigs, @Nullable List<Module> extensions) {
        this.eventConfigs = Preconditions.checkNotNull(eventConfigs, "Event configs shouldn't be null");
        this.extensionsMap = (null == extensions) ? InstanceOfMap.empty() : InstanceOfMap.<Module>builder().fromList(extensions);
    }

    @Override
    protected void configure() {
        try {

            this.extensionsMap.values().forEach(this::install);

            Map<String, Class<? extends Service>> eventSources = mapEventSources();

            final Multibinder<Service> eventSourceMultibinder = Multibinder.newSetBinder(binder(), Service.class);

            for (Configuration.EventConfig event : eventConfigs) {
                if (Strings.isNullOrEmpty(event.getSource())){
                    binder().addError("Event source is not specified for event with id '%s'", event.getId());
                    continue;
                }

                if (!eventSources.containsKey(event.getSource())) {
                    binder().addError("Unable to find event source with name '%s'. Available sources: \n%s", event.getSource(), Joiner.on('\n').join(eventSources.keySet()));
                } else {
                    install(new EventSourcePrivateModule(event, eventSourceMultibinder, eventSources.get(event.getSource())));

                }
            }

        } catch (IOException e) {
            addError(new Message("Unable to load event handlers...", e));
        }
    }

    @Provides
    @Singleton
    @Exposed
    public ServiceManager serviceManager(Set<Service> eventSources) {
        ServiceManager serviceManager = new ServiceManager(eventSources);
        serviceManager.addListener(new ServiceManager.Listener() {
            @Override
            public void healthy() {
                LOGGER.info("Event sources have bootstrapped!");
            }

            @Override
            public void stopped() {
                LOGGER.info("Event sources have stopped");
            }
        });
        return serviceManager;
    }


    /**
     * Scans whole application classpath and finds events sources
     *
     * @return 'event source name' -> 'event handler class' map
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @VisibleForTesting
    Map<String, Class<? extends Service>> mapEventSources() throws IOException {

        /** Obtains all classpath's top level classes */
        Set<ClassPath.ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader()).getAllClasses();
        LOGGER.info("Scanning classpath for EventHandlers....");

        /* iterates over all classes, filter by HandlesEvent annotation and transforms stream to needed form */
        Map<String, Class<? extends Service>> collected = classes.parallelStream()
                /* loads class infos */
                .map(classInfo -> {
                    try {
                        /* sometimes exception occurs during class loading. Return empty/absent in this case */
                        return Optional.<Class<?>>of(classInfo.load());
                    } catch (Exception | NoClassDefFoundError | IncompatibleClassChangeError e) {
                        LOGGER.trace("Class cannot be loaded: {}", classInfo.getName(), e);
                        return Optional.<Class<?>>empty();
                    }
                })
                /* filters classes which is present and marked with HandlesEvent annotation */
                .filter(((Predicate<Optional<Class<?>>>) Optional::isPresent)
                        .and(classOptional -> classOptional.get().isAnnotationPresent(EventSource.class))
                        .and(classOptional -> Service.class.isAssignableFrom(classOptional.get())))
                /* transforms from Optional<Class> to Class (obtaining values from Optionals) */
                .map(optional -> (Class<? extends Service>) optional.get())
                .collect(Collectors.toMap(clazz -> clazz.getAnnotation(EventSource.class).value(), clazz -> clazz));

        LOGGER.info("Found {} event handlers", collected.size());
        return collected;
    }


    /**
     * {@link com.google.inject.PrivateModule} binds event source type to some particular event and exposes it to parent module
     */
    private class EventSourcePrivateModule extends PrivateModule {

        private Configuration.EventConfig event;

        private Multibinder<Service> multibinder;

        private Class<? extends Service> eventSourceClass;

        public EventSourcePrivateModule(Configuration.EventConfig event, Multibinder<Service> multibinder, Class<? extends Service> eventSourceClass) {
            this.event = event;
            this.multibinder = multibinder;
            this.eventSourceClass = eventSourceClass;
        }

        @Override
        protected void configure() {
            LOGGER.info("Registering event source [{}] for event [{}]", eventSourceClass.getSimpleName(), event.getId());
            validateEvent();

            binder().bind(Duration.class).annotatedWith(Frequency.class).toInstance(Duration.ofSeconds(event.getFrequency()));
            binder().bind(String.class).annotatedWith(EventId.class).toInstance(event.getId());

            Key<Service> eventSourceKey = Key.get(Service.class, Names.named(event.getId()));
            binder().bind(eventSourceKey).to(eventSourceClass);

            expose(eventSourceKey);

            multibinder.addBinding().to(eventSourceKey);

            if (null != event.getProperties()) {
                event.getProperties().entrySet().forEach(entry ->
                        bindProperty(entry.getKey(), entry.getValue()));
            }

            /* each event handler may have own explicit guice configuration. Install it if so */
            if (!EventSource.NOP.class.equals(eventSourceClass.getAnnotation(EventSource.class).explicitConfiguration())) {
                LOGGER.info("       Registering extension module for event source [{}]", eventSourceClass.getSimpleName());
                Class<? extends Module> extensionModuleClass = eventSourceClass.getAnnotation(EventSource.class).explicitConfiguration();
                Module extensionModule = extensionsMap.getInstanceOf(extensionModuleClass);

                /* if extension module is not provided explicitly, let's create it in private module scope
                 *  otherwise it should be already installed in EventsModule
                 */
                if (null == extensionModule) {
                    try {
                        install(extensionModuleClass.getConstructor().newInstance());
                    } catch (InvocationTargetException | InstantiationException e) {
                        LOGGER.error("Unable to initialize extension", e);
                        addError("Unable to create instance of extension module '%s' for event with ID '%s'. Exception '%s'", extensionModuleClass, event.getId(), e.getMessage());
                    } catch (NoSuchMethodException | IllegalAccessException e) {
                        LOGGER.error("Unable to initialize extension", e);
                        addError("Unable to create instance of extension module '%s' for event with ID '%s'. " +
                                "Look like it doesn't have default constructor. Please, register it explicitly", extensionModuleClass, event.getId());
                    }
                }

            }

        }

        @SuppressWarnings("unchecked")
        private <T> void bindProperty(String key, T value) {
            TypeLiteral<T> type = TypeLiteral.get((Class<T>) value.getClass());
            /* make possible to use Optional<T> injections */
            OptionalBinder.newOptionalBinder(binder(), Key.get(type, Names.named(key))).setBinding().toInstance(value);
        }

        private void validateEvent() {
            if (event.getFrequency() <= 0) {
                binder().addError("Frequency of event with ID '%s' is not specified", event.getId());
            }

            if (null == event.getId()) {
                binder().addError("ID of event with ID '%s' is not specified", event.getId());
            }
        }
    }


}

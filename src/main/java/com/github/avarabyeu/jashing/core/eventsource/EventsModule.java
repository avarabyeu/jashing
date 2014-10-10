package com.github.avarabyeu.jashing.core.eventsource;

import com.github.avarabyeu.jashing.core.Configuration;
import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.core.eventsource.annotation.Frequency;
import com.github.avarabyeu.jashing.exception.IncorrectConfigurationException;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Guice configuration {@link com.google.inject.Module} for event handlers. Create all defined in configuration event handlers,
 * populates them with needed values and registers as services
 *
 * @author avarabyeu
 */
public class EventsModule extends AbstractModule {

    private List<Configuration.EventConfig> eventConfigs;

    @Inject
    public EventsModule(List<Configuration.EventConfig> eventConfigs) {
        this.eventConfigs = Preconditions.checkNotNull(eventConfigs, "Event configs shouldn't be null");
    }

    @Override
    protected void configure() {
        try {
            Map<String, Class<?>> eventHandlers = EventUtils.mapEventHandlers();

            final Multibinder<EventSource> eventSourceMultibinder = Multibinder.newSetBinder(binder(), EventSource.class);

            eventConfigs.stream().forEach(event -> {

                if (!eventHandlers.containsKey(event.getType())) {
                    throw new IncorrectConfigurationException("Unable to find handler for event with type '" + event.getType() + "'");
                }

                install(new PrivateModule() {
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
                            event.getProperties().entrySet().forEach(entry -> {
                                bindProperty(entry.getKey(), entry.getValue());
                            });
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


}

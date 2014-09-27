package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.Configuration;
import com.github.avarabyeu.jashing.exception.IncorrectConfigurationException;
import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            List<EventSource> eventSources = new ArrayList<>();
            eventConfigs.stream().forEach(event -> {

                if (!eventHandlers.containsKey(event.getType())) {
                    throw new IncorrectConfigurationException("Unable to find handler for event with type '" + event.getType() + "'");
                }

                install(new PrivateModule() {
                    @Override
                    protected void configure() {
                        try {
                            Class<? extends EventSource> handlerClass = (Class<? extends EventSource>) eventHandlers.get(event.getType());
                            EventSource eventSource = TypeToken.of(handlerClass).constructor(handlerClass.getConstructor(String.class, Duration.class))
                                    .invoke(null, event.getId(), Duration.ofSeconds(event.getFrequency()));

                            if (null != event.getProperties()) {
                                event.getProperties().entrySet().forEach(entry -> {
                                    bindProperty(entry.getKey(), entry.getValue());
                                });
                            }

                            binder().requestInjection(eventSource);
                            eventSources.add(eventSource);

                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            throw new IncorrectConfigurationException("Unable to load event handler class", e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    private <T> void bindProperty(String key, T value) {
                        TypeLiteral<T> type = TypeLiteral.get((Class<T>) value.getClass());
                        bind(type).annotatedWith(Names.named(key)).toInstance(value);
                    }
                });


            });

            ServiceManager instance = new ServiceManager(eventSources);
            binder().bind(ServiceManager.class).toInstance(instance);


        } catch (IOException e) {
            throw new IncorrectConfigurationException("Unable to load event handlers...", e);
        }
    }


}

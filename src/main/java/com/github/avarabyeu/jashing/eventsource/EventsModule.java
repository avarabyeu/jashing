package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.Configuration;
import com.github.avarabyeu.jashing.exception.IncorrectConfigurationException;
import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.apache.commons.beanutils.BeanUtils;

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

    private Configuration configuration;

    @Inject
    public EventsModule(Configuration configuration) {
        this.configuration = Preconditions.checkNotNull(configuration, "Configuration shouldn't be null");
    }

    @Override
    protected void configure() {
        try {
            Map<String, Class<?>> eventHandlers = EventUtils.mapEventHandlers();

            List<EventSource> eventSources = new ArrayList<>();
            configuration.getEvents().stream().forEach(event -> {
                try {
                    if (!eventHandlers.containsKey(event.getType())) {
                        throw new IncorrectConfigurationException("Unable to find handler for event with type '" + event.getType() + "'");
                    }

                    Class<?> handlerClass = eventHandlers.get(event.getType());
                    EventSource eventSource = (EventSource) TypeToken.of(handlerClass).constructor(handlerClass.getConstructor(String.class, Duration.class))
                            .invoke(null, event.getId(), Duration.ofSeconds(event.getFrequency()));

                    BeanUtils.populate(eventSource, event.getProperties());
                    binder().requestInjection(eventSource);
                    eventSources.add(eventSource);

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new IncorrectConfigurationException("Unable to load event handler class", e);
                }
            });

            ServiceManager instance = new ServiceManager(eventSources);
            binder().bind(ServiceManager.class).toInstance(instance);


        } catch (IOException e) {
            throw new IncorrectConfigurationException("Unable to load event handlers...", e);
        }
    }


}

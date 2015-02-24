package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.core.subscribers.JashingEventHandler;
import com.github.avarabyeu.jashing.core.subscribers.LoggingSubscriberExceptionHandler;
import com.github.avarabyeu.jashing.core.subscribers.ServerSentEventHandler;
import com.google.common.base.Charsets;
import com.google.common.eventbus.EventBus;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.Service;
import com.google.gson.Gson;
import com.google.inject.*;
import com.google.inject.name.Names;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Main application configuration module. Starts HTTP server and all necessary stuff
 *
 * @author avarabyeu
 */
class JashingModule extends AbstractModule {

    private static final String APPLICATION_CONFIG = "config.json";

    private final Integer port;

    private final List<Module> extensions;

    public JashingModule(List<Module> extensions) {
        this(null, extensions);
    }

    public JashingModule(@Nullable Integer port, List<Module> extensions) {
        this.port = port;
        this.extensions = extensions;
    }

    @Override
    protected void configure() {

        binder().requireExplicitBindings();

        /* Event Bus. In charge of dispatching events from message sources to event handlers */
        final EventBus eventBus = new EventBus(new LoggingSubscriberExceptionHandler());
        binder().bind(EventBus.class).toInstance(eventBus);
        binder().bind(ServerSentEventHandler.class).to(JashingEventHandler.class);


        Gson gson = new Gson();
        binder().bind(Gson.class).toInstance(gson);

        /* binds properties. Replaces property files with json-based configuration. Just to have all events-related properties in one file */
        Configuration configuration = provideConfiguration(gson);
        configuration.getProperties().entrySet().forEach(entry -> binder().bindConstant().annotatedWith(Names.named(entry.getKey())).to(entry.getValue()));

        extensions.stream().forEach(this::install);

        /* install module with events configuration */
        binder().install(new EventsModule(configuration.getEvents()));

        binder().bind(JashingController.class).in(Scopes.SINGLETON);

    }


    @Provides
    @Singleton
    public JashingServer application(EventBus eventBus, JashingController jashingController) {
        JashingServer jashing = new JashingServer(port, jashingController);
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownServiceTask(jashing, eventBus)));
        return jashing;
    }


    public static class ShutdownServiceTask implements Runnable {
        private final Service service;
        private final EventBus eventBus;

        ShutdownServiceTask(Service service, EventBus eventBus) {
            this.service = service;
            this.eventBus = eventBus;
        }

        @Override
        public void run() {
            this.eventBus.post(new ShutdownEvent());
            this.service.stopAsync().awaitTerminated();
        }
    }


    private Configuration provideConfiguration(Gson gson) {
        try {
            URL config = Thread.currentThread().getContextClassLoader().getResource(APPLICATION_CONFIG);
            assert config != null;
            return gson.fromJson(Resources.asCharSource(config, Charsets.UTF_8).openBufferedStream(), Configuration.class);
        } catch (IOException e) {
            throw new IncorrectConfigurationException("Unable to read configuration");

        }
    }


}

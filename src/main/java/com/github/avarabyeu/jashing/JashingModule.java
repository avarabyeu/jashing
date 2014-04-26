package com.github.avarabyeu.jashing;

import com.github.avarabyeu.jashing.events.ShutdownEvent;
import com.github.avarabyeu.jashing.eventsource.EventsModule;
import com.github.avarabyeu.jashing.subscribers.JashingEventHandler;
import com.github.avarabyeu.jashing.subscribers.LoggingSubscriberExceptionHandler;
import com.github.avarabyeu.jashing.subscribers.ServerSentEventHandler;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Service;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Created by andrey.vorobyov on 25/04/14.
 */
public class JashingModule extends AbstractModule {


    private final LaunchProperties launchProperties;

    public JashingModule(LaunchProperties launchProperties) {
        this.launchProperties = launchProperties;
    }

    @Override
    protected void configure() {

        binder().requireExplicitBindings();

        final EventBus eventBus = new EventBus(new LoggingSubscriberExceptionHandler());
        binder().bind(EventBus.class).toInstance(eventBus);
        binder().bind(ServerSentEventHandler.class).to(JashingEventHandler.class);
        binder().bind(Gson.class);

        binder().install(new EventsModule());
    }


    @Provides
    @Singleton
    public JashingServer application(EventBus eventBus, Provider<ServerSentEventHandler> serverSentEventHandlerProvider) {
        JashingServer jashing = new JashingServer(launchProperties.getPort(), serverSentEventHandlerProvider);
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


}

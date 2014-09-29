package com.github.avarabyeu.jashing;

import com.github.avarabyeu.jashing.events.ShutdownEvent;
import com.github.avarabyeu.jashing.eventsource.EventsModule;
import com.github.avarabyeu.jashing.exception.IncorrectConfigurationException;
import com.github.avarabyeu.jashing.integration.vcs.VCSClient;
import com.github.avarabyeu.jashing.integration.vcs.svn.SvnClient;
import com.github.avarabyeu.jashing.subscribers.JashingEventHandler;
import com.github.avarabyeu.jashing.subscribers.LoggingSubscriberExceptionHandler;
import com.github.avarabyeu.jashing.subscribers.ServerSentEventHandler;
import com.google.common.base.Charsets;
import com.google.common.eventbus.EventBus;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.Service;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URL;

/**
 * Main application configuration module. Starts HTTP server and all necessary stuff
 *
 * @author avarabyeu
 */
public class JashingModule extends AbstractModule {


    public static final String APPLICATION_CONFIG = "config.json";

    /* Bootstrap properties */
    private final BootstrapProperties bootstrapProperties;

    public JashingModule(BootstrapProperties bootstrapProperties) {
        this.bootstrapProperties = bootstrapProperties;
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

        /* binds properties. Replaces property files with json-based configuration. Just to have all properties in one file */
        Configuration configuration = provideConfiguration(gson);
        configuration.getProperties().entrySet().forEach(entry -> binder().bindConstant().annotatedWith(Names.named(entry.getKey())).to(entry.getValue()));

        /* install module with events configuration */
        binder().install(new EventsModule(configuration.getEvents()));
    }


    @Provides
    @Singleton
    public JashingServer application(EventBus eventBus, Provider<ServerSentEventHandler> serverSentEventHandlerProvider) {
        JashingServer jashing = new JashingServer(bootstrapProperties.getPort(), serverSentEventHandlerProvider);
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownServiceTask(jashing, eventBus)));
        return jashing;
    }

    @Inject
    @Provides
    public VCSClient provideSvnClient(@Named("svnUrl") String url, @Named("svnUser") String user, @Named("svnPassword") String password) {
        return new SvnClient(url, user, password);
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


    public Configuration provideConfiguration(Gson gson) {
        try {
            URL config = Thread.currentThread().getContextClassLoader().getResource(APPLICATION_CONFIG);
            assert config != null;
            return gson.fromJson(Resources.asCharSource(config, Charsets.UTF_8).openBufferedStream(), Configuration.class);
        } catch (IOException e) {
            throw new IncorrectConfigurationException("Unable to read configuration");

        }
    }


}

package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.core.subscribers.*;
import com.github.avarabyeu.jashing.utils.ResourceUtils;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.gson.Gson;
import com.google.inject.*;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Main application configuration module. Configures server and all necessary stuff
 *
 * @author avarabyeu
 */
class JashingModule extends AbstractModule {

    private static final Integer DEFAULT_PORT = 8181;

    private static final Logger LOGGER = LoggerFactory.getLogger(JashingModule.class);

    private static final String APPLICATION_CONFIG = "config.json";
    private static final String TIMEOUT_PROPERTY = "timeout";

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

        Gson gson = new Gson();
        binder().bind(Gson.class).toInstance(gson);

        /* binds properties. Replaces property files with json-based configuration. Just to have all events-related properties in one file */
        Configuration configuration = provideConfiguration(gson);
        Map<String, String> globalProperties = configuration.getProperties();
        globalProperties.entrySet().forEach(entry -> binder().bindConstant().annotatedWith(Names.named(entry.getKey())).to(entry.getValue()));

        OptionalBinder<Integer> serverPort = OptionalBinder.newOptionalBinder(binder(), Key.get(Integer.class, Names.named("serverPort")));
        serverPort.setDefault().toInstance(DEFAULT_PORT);
        if (null != this.port) {
            serverPort.setBinding().toInstance(this.port);
        }

        OptionalBinder<Long> timeoutBinder = OptionalBinder.newOptionalBinder(binder(), Key.get(Long.class, Timeout.class));
        if (globalProperties.containsKey(TIMEOUT_PROPERTY)) {
            timeoutBinder.setBinding().toInstance(Long.valueOf(globalProperties.get(TIMEOUT_PROPERTY)));
        }

        

        /* install module with events configuration */
        binder().install(new EventsModule(configuration.getEvents(), extensions));

        binder().bind(JashingController.class).in(Scopes.SINGLETON);

    }


    @Provides
    @Singleton
    public JashingServer jashingServer(JashingController jashingController) {
        JashingServer jashing = new JashingServer(port, jashingController);
        jashing.addListener(new Service.Listener() {
            @Override
            public void running() {
                LOGGER.info("Embedded Jashing server has started on port [{}]", port);
            }

            @Override
            public void stopping(Service.State from) {
                LOGGER.info("Stopping embedded Jashing server");
            }
        }, MoreExecutors.directExecutor());
        return jashing;
    }


    @Provides
    public ServerSentEventsHandler serverSentEventHandler(EventBus eventBus, Gson gson, @Timeout Optional<Long> timeout) {
        //return new RateLimitingDecorator(new ServerSentEventHandlerImpl(eventBus, gson), timeout);
        return new ServerSentEventHandlerImpl(eventBus, gson);
    }

    private Configuration provideConfiguration(Gson gson) {
        try {
            URL config = ResourceUtils.getResourceAsURL(APPLICATION_CONFIG);
            Preconditions.checkState(config != null, "Main application config [%s] not found", APPLICATION_CONFIG);
            return gson.fromJson(Resources.asCharSource(config, Charsets.UTF_8).openBufferedStream(), Configuration.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read configuration", e);

        }
    }


}

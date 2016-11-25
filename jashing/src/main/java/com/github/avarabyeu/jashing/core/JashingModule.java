package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.utils.ResourceUtils;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        final EventBus eventBus = new EventBus(
                (exception, context) -> LOGGER.error("Could not dispatch event: {} to {}",
                        context.getSubscriber(), context.getSubscriberMethod(),
                        exception.getCause()));

        binder().bind(EventBus.class).toInstance(eventBus);

        Gson gson = new Gson();
        binder().bind(Gson.class).toInstance(gson);

        /* binds properties. Replaces property files with json-based configuration. Just to have all events-related properties in one file */
        Configuration configuration = provideConfiguration(gson);
        final Map<String, String> properties = configuration.getProperties();

        /* replace with JVM arg if exist */
        Map<String, String> globalProperties =
                properties.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey, e -> System.getProperty(e.getKey(), e.getValue())));

        globalProperties.entrySet().forEach(
                entry -> binder().bindConstant().annotatedWith(Names.named(entry.getKey())).to(entry.getValue()));


        /* install module with events configuration */
        binder().install(new EventsModule(configuration.getEvents(), extensions));

        binder().bind(JashingServer.class).toProvider(() -> {
            Optional<Long> timeout = globalProperties.containsKey(TIMEOUT_PROPERTY) ?
                    Optional.of(Long.valueOf(globalProperties.get(TIMEOUT_PROPERTY))) :
                    Optional.empty();
            JashingServer jashing = new JashingServer(this.port == null ? DEFAULT_PORT : this.port, eventBus, gson,
                    timeout);
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
        }).in(Scopes.SINGLETON);

    }

    private Configuration provideConfiguration(Gson gson) {
        try {
            URL config = ResourceUtils.getResourceAsURL(APPLICATION_CONFIG);
            Preconditions.checkState(config != null, "Main application config [%s] not found", APPLICATION_CONFIG);
            return gson
                    .fromJson(Resources.asCharSource(config, Charsets.UTF_8).openBufferedStream(), Configuration.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read configuration", e);

        }
    }

}

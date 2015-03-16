package com.github.avarabyeu.jashing.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Application Entry Point. Creates Guava's Injector and runs spark server
 *
 * @author avarabyeu
 */
public class Jashing {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jashing.class);

    private AtomicBoolean bootstrapped;
    private Injector injector;
    private Mode mode;
    private Thread shutdownHook;


    private Jashing(Injector injector, Mode mode) {
        this.injector = injector;
        this.mode = mode;
        this.bootstrapped = new AtomicBoolean(false);
        this.shutdownHook = new Thread(this::shutdown);
    }

    /**
     * Bootstaps Jashing. This operation is allowed only once. Bootstrapping already started Jashing is not permitted
     *
     * @return yourself
     */
    public Jashing bootstrap() {
        if (bootstrapped.compareAndSet(false, true)) {
            mode.bootstrap(injector);
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            LOGGER.info("Jashing has started!");
        } else {
            throw new IllegalStateException("Jashing already bootstrapped");
        }
        return this;
    }

    /**
     * Shutdowns Jashing. Permitted only for bootstrapped instance
     */
    public void shutdown() {
        if (bootstrapped.compareAndSet(true, false)) {
            LOGGER.info("Shutting down Jashing...");
            mode.shutdown(injector);

            /* shutdown method might be called by this hook. So, trying to remove
             * hook which is currently is progress causes error
             */
            if (!shutdownHook.isAlive()) {
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            }
            LOGGER.info("Jashing has stopped.");
        } else {
            throw new IllegalStateException("Jashing is not bootstrapped");
        }
    }

    /**
     * Obtains application controller. Used in {@link com.github.avarabyeu.jashing.core.JashingFilter} for bootstrapping {@link javax.servlet.Filter}
     *
     * @return Jashing controller
     */
    JashingController getController() {
        return injector.getInstance(JashingController.class);
    }

    /**
     * Creates new Jashing builder
     *
     * @return Builder of Jashing instance
     */
    public static Builder builder() {
        return new Builder();
    }


    public static void main(String... args) throws InterruptedException, IOException {
        try {
            Jashing.builder().build(Mode.EMBEDDED).bootstrap();
        } catch (Exception e) {
            LOGGER.error("Jashing can't started", e);
        }

    }

    /**
     * Jashing builder
     */
    public static class Builder {

        /* List of extension-modules */
        private List<Module> modules = new LinkedList<>();

        private Integer port;


        /**
         * Port for embedded mode.
         *
         * @param port port Jashing should start on
         * @return this builder
         */
        public Builder withPort(int port) {
            Preconditions.checkArgument(0 < port, "Port incorrect port number %s", port);
            this.port = port;
            return this;
        }

        /**
         * Registers extension modules
         *
         * @param modules Array of extension modules
         * @return this builder
         */
        public Builder registerModule(@Nullable Module... modules) {
            if (null != modules) {
                Collections.addAll(this.modules, modules);
            }
            return this;
        }

        /**
         * Builds jashing in Embedded mode
         *
         * @return Jashing instance
         */
        public Jashing build() {
            return build(Mode.EMBEDDED);
        }

        /**
         * Builds Jashing instance for specified {@link com.github.avarabyeu.jashing.core.Jashing.Mode}
         * Package-private to hide {@link com.github.avarabyeu.jashing.core.Jashing.Mode#CONTAINER} to internal implementation
         *
         * @param mode Jashing mode
         * @return Jashing
         */
        Jashing build(@Nonnull Mode mode) {
            Injector injector =
                    Guice.createInjector(new JashingModule(port, ImmutableList.<Module>builder()
                            .addAll(modules).build()));


            return new Jashing(injector, mode);
        }
    }

    /**
     * Specifies mode Jashing running in
     * May be container (when Jashing is deployed into Servlet Container) or embedded (Jashing starts embedded container)
     */
    static enum Mode {
        EMBEDDED {
            /**
             * In embedded mode we need to start separate embedded server
             */
            @Override
            void bootstrap(Injector injector) {
                CONTAINER.bootstrap(injector);

                /* bootstrap server */
                Service application = injector.getInstance(JashingServer.class);
                application.startAsync();

            }

            @Override
            void shutdown(Injector injector) {
                CONTAINER.shutdown(injector);
                injector.getInstance(JashingServer.class).stopAsync().awaitTerminated();
            }
        },
        CONTAINER {
            /**
             * In container Mode we need to bootstrap event sources only. Servlet container takes care about server-related stuff
             */
            @Override
            void bootstrap(Injector injector) {
                /* bootstrap event sources* */
                ServiceManager eventSources = injector.getInstance(ServiceManager.class);
                eventSources.startAsync();
            }

            /**
             * In container mode we need to notify all event sources about shutting down (to release all active connections and unsubscribe from event sources)
             * and stop all event sources
             */
            @Override
            void shutdown(Injector injector) {
                injector.getInstance(EventBus.class).post(new ShutdownEvent());
                injector.getInstance(ServiceManager.class).stopAsync().awaitStopped();
            }
        };

        abstract void bootstrap(Injector injector);

        abstract void shutdown(Injector injector);
    }
}

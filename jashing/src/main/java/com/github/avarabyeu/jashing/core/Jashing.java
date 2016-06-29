package com.github.avarabyeu.jashing.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class Jashing {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jashing.class);

    private AtomicBoolean bootstrapped;
    private Injector injector;
    private Thread shutdownHook;

    private Jashing(Injector injector) {
        this.injector = injector;
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

            /* bootstrap event sources* */
            ServiceManager eventSources = injector.getInstance(ServiceManager.class);
            eventSources.startAsync();

            /* bootstrap server */
            Service application = injector.getInstance(JashingServer.class);
            application.startAsync();

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

            injector.getInstance(ServiceManager.class).stopAsync().awaitStopped();
            injector.getInstance(JashingServer.class).stopAsync().awaitTerminated();


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
     * Creates new Jashing builder
     *
     * @return Builder of Jashing instance
     */
    public static Builder builder() {
        return new Builder();
    }

    public static void main(String... args) throws InterruptedException, IOException {
        try {
            Jashing.builder().build().bootstrap();
        } catch (Exception e) {
            LOGGER.error("Jashing cannot start", e);
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
         * Builds Jashing instance
         *
         * @return Jashing
         */
        public Jashing build() {
            Injector createdInjector =
                    Guice.createInjector(new JashingModule(port, ImmutableList.<Module>builder()
                            .addAll(modules).build()));

            return new Jashing(createdInjector);
        }
    }

}

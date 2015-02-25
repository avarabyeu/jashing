package com.github.avarabyeu.jashing.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

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

    private AtomicBoolean bootstrapped;
    private Injector injector;


    private Jashing(Injector injector) {
        this.injector = injector;
        this.bootstrapped = new AtomicBoolean(false);
    }

    Jashing bootstrap() {
        if (bootstrapped.compareAndSet(false, true)) {
            bootstrapEventSources();
        } else {
            throw new IllegalStateException("Jashing already bootstrapped");
        }
        return this;
    }

    public void bootstrapEmbedded() {
        if (bootstrapped.compareAndSet(false, true)) {
            bootstrapEventSources();

            /* bootstrap server */
            Service application = injector.getInstance(JashingServer.class);
            application.startAsync();
        } else {
            throw new IllegalStateException("Jashing already bootstrapped");
        }
    }

    public void shutdown() {
        if (bootstrapped.compareAndSet(true, false)) {
            injector.getInstance(EventBus.class).post(new ShutdownEvent());
            injector.getInstance(ServiceManager.class).stopAsync().awaitStopped();
        } else {
            throw new IllegalStateException("Jashing is not bootstrapped");
        }
    }

    JashingController getController() {
        return injector.getInstance(JashingController.class);
    }

    private void bootstrapEventSources() {
        /* bootstrap event sources* */
        ServiceManager eventSources = injector.getInstance(ServiceManager.class);
        eventSources.startAsync();
    }

    public static Builder newOne() {
        return new Builder();
    }


    public static void main(String... args) throws InterruptedException, IOException {
        try {
            Jashing.newOne().build().bootstrapEmbedded();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class Builder {

        private List<Module> modules = new LinkedList<>();

        private Integer port;


        public Builder withPort(int port) {
            Preconditions.checkArgument(0 < port, "Port incorrect port number %s", port);
            this.port = port;
            return this;
        }

        public Builder registerModule(Module module) {
            modules.add(module);
            return this;
        }

        public Builder registerModule(Module... modules) {
            if (null != modules) {
                Collections.addAll(this.modules, modules);
            }
            return this;
        }

        public Builder registerModule(List<Module> modules) {
            if (null != modules) {
                this.modules.addAll(modules);
            }
            return this;
        }

        public Jashing build() {
            Injector injector =
                    Guice.createInjector(new JashingModule(port, ImmutableList.<Module>builder()
                            .addAll(modules).build()));


            return new Jashing(injector);
        }
    }
}

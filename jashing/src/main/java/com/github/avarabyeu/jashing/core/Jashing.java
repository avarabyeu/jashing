package com.github.avarabyeu.jashing.core;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Application Entry Point. Creates Guava's Injector and runs spark server
 *
 * @author avarabyeu
 */
public class Jashing {

    private List<Module> modules;

    private Integer port;

    public Jashing() {
        modules = new LinkedList<>();
        port = null;
    }

    public Jashing registerModule(Module module) {
        modules.add(module);
        return this;
    }

    public void start() {
        Injector injector = Guice.createInjector(new JashingModule(port, ImmutableList.<Module>builder()
                .addAll(modules).build()));

            /* bootstrap server */
        Service application = injector.getInstance(JashingServer.class);
        application.startAsync();

        /* bootstrap event sources* */
        ServiceManager eventSources = injector.getInstance(ServiceManager.class);
        eventSources.startAsync();

    }

    public static void main(String... args) throws InterruptedException, IOException {
        try {
            new Jashing().start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

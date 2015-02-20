package com.github.avarabyeu.jashing.core;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

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

    public Jashing() {
        modules = new LinkedList<>();
    }

    public Jashing registerModule(Module module) {
        modules.add(module);
        return this;
    }

    public void start() {

        BootstrapProperties bootstrapProperties = new BootstrapProperties();
        Injector injector = Guice.createInjector(new JashingModule(bootstrapProperties, ImmutableList.<Module>builder()
                .addAll(modules).build()));

            /* bootstrap server */
        Service application = injector.getInstance(JashingServer.class);
        application.startAsync();

        /* bootstrap event sources* */
        ServiceManager eventSources = injector.getInstance(ServiceManager.class);
        eventSources.startAsync();

        /* holds current thread while server is alive */
        application.awaitTerminated();

    }

    public static void main(String... args) throws InterruptedException, IOException {
        BootstrapProperties bootstrapProperties = new BootstrapProperties();
        CmdLineParser cmdLineParser = new CmdLineParser(bootstrapProperties);
        try {
            /* Parse CLI arguments */
            cmdLineParser.parseArgument(args);
            new Jashing().start();

        } catch (CmdLineException e) {
            e.printStackTrace();
            e.getParser().printUsage(System.err);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

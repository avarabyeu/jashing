package com.github.avarabyeu.jashing;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.lang.reflect.Parameter;

/**
 * Application Entry Point. Creates Guava's Injector and runs spark server
 *
 * @author avarabyeu
 */
public class Jashing {

    public static void main(String... args) throws InterruptedException, IOException {
        BootstrapProperties bootstrapProperties = new BootstrapProperties();
        CmdLineParser cmdLineParser = new CmdLineParser(bootstrapProperties);
        try {
            /* Parse CLI arguments */
            cmdLineParser.parseArgument(args);
            Injector injector = Guice.createInjector(new JashingModule(bootstrapProperties));


            /* bootstrap server */
            Service application = injector.getInstance(JashingServer.class);
            application.startAsync();

            /* bootstrap event sources* */
            ServiceManager eventSources = injector.getInstance(ServiceManager.class);
            eventSources.startAsync();
        } catch (CmdLineException e) {
            e.printStackTrace();
            e.getParser().printUsage(System.err);
        }

    }
}

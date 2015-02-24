package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.core.eventsource.TestEventSource;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.ServiceManager;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * @author Andrei Varabyeu
 */
public class ConfigurationReaderTest {

    @Test
    public void readConfiguration() throws IOException {
        Configuration configuration;
        Gson gson = new Gson();

        try {
            URL config = Thread.currentThread().getContextClassLoader().getResource("test-config.json");
            assert config != null;
            configuration = gson.fromJson(Resources.asCharSource(config, Charsets.UTF_8).openBufferedStream(), Configuration.class);
        } catch (IOException e) {
            throw new IncorrectConfigurationException("Unable to read configuration");

        }

        Injector injector = Guice.createInjector(new EventsModule(configuration.getEvents()));
        ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        TestEventSource eventSource = (TestEventSource) serviceManager.servicesByState().values().stream().filter(service -> TestEventSource.class.equals(service.getClass())).findFirst().get();

        Assert.assertEquals("string", eventSource.getPropertyString());
        System.out.println("OK!");
        Assert.assertEquals(100, eventSource.getPropertyInt(), 0);

        System.out.println();

        TestEventSource eventSource2 = (TestEventSource) serviceManager.servicesByState().values().stream().filter(service -> TestEventSource.class.equals(service.getClass()) && ((TestEventSource) service).getPropertyInt().equals((double)200))
                .findFirst().get();
//

        System.out.println(Joiner.on('\n').join(serviceManager.servicesByState().values()));


    }
}

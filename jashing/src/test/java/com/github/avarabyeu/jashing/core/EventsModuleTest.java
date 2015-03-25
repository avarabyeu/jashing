package com.github.avarabyeu.jashing.core;

import com.github.avarabyeu.jashing.core.eventsource.TestEventSource;
import com.github.avarabyeu.jashing.utils.ResourceUtils;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.gson.Gson;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Andrei Varabyeu
 */
public class EventsModuleTest {

    private static Configuration configuration;

    private static Injector injector;
    private static EventsModule eventsModule;

    @BeforeClass
    public static void prepare() throws IOException {
        configuration = new Gson().fromJson(ResourceUtils.getResourceAsByteSource("test-config.json").asCharSource(Charsets.UTF_8).openBufferedStream(), Configuration.class);
        eventsModule = new EventsModule(configuration.getEvents());
        injector = Guice.createInjector(eventsModule);


    }

    @Test(expected = ConfigurationException.class)
    public void testEventSourceIsNotExposed() {
        injector.getInstance(TestEventSource.class);
    }

    @Test
    public void testMapEventHandlers() throws IOException {
        Map<String, Class<? extends Service>> eventHandlersMap = eventsModule.mapEventSources();
        Class<? extends Service> testEventHandlers = eventHandlersMap.get("test");
        Assert.assertThat(testEventHandlers, notNullValue());
        System.out.println(testEventHandlers);
    }

    @Test
    public void checkEventSourcePropertiesBinding() {
        ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        ImmutableList<Service> eventSources = serviceManager.servicesByState().values().asList();

        Assert.assertThat(eventSources, hasSize(2));

        TestEventSource firstEventSource = (TestEventSource) eventSources.get(0);
        Assert.assertEquals("first", firstEventSource.getPropertyString());
        Assert.assertEquals(100, firstEventSource.getPropertyInt(), 0);

        TestEventSource secondEventSource = (TestEventSource) eventSources.get(1);
        Assert.assertEquals("second", secondEventSource.getPropertyString());
        Assert.assertEquals(200, secondEventSource.getPropertyInt(), 0);
    }


}

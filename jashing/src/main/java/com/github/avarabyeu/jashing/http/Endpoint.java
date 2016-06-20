package com.github.avarabyeu.jashing.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ro.isdc.wro.config.factory.PropertiesAndFilterConfigWroConfigurationFactory;
import ro.isdc.wro.config.jmx.WroConfiguration;
import ro.isdc.wro.http.WroFilter;
import ro.isdc.wro.manager.factory.BaseWroManagerFactory;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;
import ro.isdc.wro.util.ObjectFactory;

import javax.servlet.FilterConfig;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by avarabyeu on 11/28/15.
 */
public class Endpoint {

    public void start(Router router) throws Exception {
        // Create a basic jetty server object that will listen on port 8080.
        // Note that if you set this to port 0 then a randomly available port
        // will be assigned that you can either look in the logs for the port,
        // or programmatically obtain it for use in test cases.
        Server server = new Server(8080);

        // The ServletHandler is a dead simple way to create a context handler
        // that is backed by an instance of a Servlet.
        // This handler then needs to be registered with the Server object.
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(new ServletHolder(new RouterServlet(router)), "/*");

        WroFilter wroFilter = new WroFilter() {
            @Override
            protected ObjectFactory<WroConfiguration> newWroConfigurationFactory(FilterConfig filterConfig) {
                return new PropertiesAndFilterConfigWroConfigurationFactory(filterConfig) {
                    @Override
                    public Properties createProperties() {
                        Properties properties = new Properties();
                        properties.putAll(super.createProperties());
                        properties.setProperty("jmxEnabled", "false");
                        properties.setProperty("managerFactoryClassName",
                                "com.github.avarabyeu.jashing.http.DemoManagerFactory");
                        return properties;
                    }
                };
            }
        };

        handler.addFilterWithMapping(new FilterHolder(wroFilter), "/wro/*", FilterMapping.REQUEST);

        server.setHandler(handler);

        //            ResourceHandler rh = new ResourceHandler();
        //            rh.setResourceBase();

        // Start things up!
        server.start();
        server.join();
    }

}

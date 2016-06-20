package com.github.avarabyeu.jashing.http;

import ro.isdc.wro.manager.factory.BaseWroManagerFactory;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;
import ro.isdc.wro.model.resource.locator.ClasspathUriLocator;
import ro.isdc.wro.model.resource.locator.UriLocator;
import ro.isdc.wro.model.resource.locator.factory.SimpleUriLocatorFactory;
import ro.isdc.wro.model.resource.locator.factory.UriLocatorFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by avarabyeu on 11/30/15.
 */
public class DemoManagerFactory extends BaseWroManagerFactory {
    @Override
    protected WroModelFactory newModelFactory() {
        return new XmlModelFactory() {
            @Override
            protected InputStream getModelResourceAsStream() throws IOException {
                final String resourceLocation = getDefaultModelFilename();
                final InputStream stream = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(resourceLocation);

                if (stream == null) {
                    throw new IOException("Invalid resource requested: " + resourceLocation);
                }

                return stream;
            }
        };
    }

//    @Override
//    protected UriLocatorFactory newUriLocatorFactory() {
//        return new SimpleUriLocatorFactory().addLocator(new ClasspathUriLocator());
//    }
}

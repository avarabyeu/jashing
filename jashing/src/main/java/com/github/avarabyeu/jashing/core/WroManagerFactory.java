package com.github.avarabyeu.jashing.core;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import ro.isdc.wro.extensions.processor.css.RubySassCssProcessor;
import ro.isdc.wro.extensions.processor.js.CoffeeScriptProcessor;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.locator.UriLocator;
import ro.isdc.wro.model.resource.locator.factory.SimpleUriLocatorFactory;
import ro.isdc.wro.model.resource.locator.factory.UriLocatorFactory;
import ro.isdc.wro.model.resource.processor.decorator.ProcessorDecorator;
import ro.isdc.wro.model.resource.processor.factory.ProcessorsFactory;
import ro.isdc.wro.model.resource.processor.factory.SimpleProcessorsFactory;
import ro.isdc.wro.model.resource.processor.impl.css.CssUrlRewritingProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import static com.google.common.io.Resources.getResource;

/**
 * Wro4j Manager Factory. Optimized for built-in usage
 *
 * @author avarabyeu
 */
class WroManagerFactory extends ConfigurableWroManagerFactory {

    private static final String COFFEE_FILENAME = ".coffee";

    @Override
    protected ProcessorsFactory newProcessorsFactory() {
        SimpleProcessorsFactory factory = new SimpleProcessorsFactory();
        factory.addPreProcessor(new ProcessorDecorator(new CoffeeScriptProcessor()) {
            public void process(Resource resource, Reader reader, Writer writer) throws IOException {
                if (resource.getUri().endsWith(COFFEE_FILENAME)) {
                    super.process(resource, reader, writer);
                } else {
                    CharStreams.copy(reader, writer);
                }
            }
        });
        factory.addPreProcessor(new CssUrlRewritingProcessor());
        factory.addPostProcessor(new RubySassCssProcessor());

        return factory;
    }

    @Override
    protected WroModelFactory newModelFactory() {
        return new XmlModelFactory() {
            @Override
            protected InputStream getModelResourceAsStream() throws IOException {
                return Resources.asByteSource(getResource("wro.xml")).openStream();
            }
        };
    }

    @Override
    protected Properties newConfigProperties() {
        Properties properties = new Properties();
        try (InputStream is = Resources.asByteSource(Resources.getResource("wro.properties")).openStream()) {
            properties.load(is);
        } catch (IOException e) {
            throw new IncorrectConfigurationException("Unable to load wro configuration");
        }

        return properties;
    }

    @Override
    protected void contributeLocators(Map<String, UriLocator> map) {
        map.put(MultipleClasspathUrlLocator.ALIAS, new MultipleClasspathUrlLocator());
    }
}

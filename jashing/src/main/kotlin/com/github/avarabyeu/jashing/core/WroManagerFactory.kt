package com.github.avarabyeu.jashing.core

import com.google.common.io.CharStreams
import com.google.common.io.Resources
import ro.isdc.wro.extensions.processor.css.RubySassCssProcessor
import ro.isdc.wro.extensions.processor.js.CoffeeScriptProcessor
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory
import ro.isdc.wro.model.factory.WroModelFactory
import ro.isdc.wro.model.factory.XmlModelFactory
import ro.isdc.wro.model.resource.Resource
import ro.isdc.wro.model.resource.processor.decorator.ProcessorDecorator
import ro.isdc.wro.model.resource.processor.factory.ProcessorsFactory
import ro.isdc.wro.model.resource.processor.factory.SimpleProcessorsFactory
import ro.isdc.wro.model.resource.processor.impl.css.CssUrlRewritingProcessor

import java.io.IOException
import java.io.InputStream
import java.io.Reader
import java.io.Writer
import java.util.Properties

import com.google.common.io.Resources.getResource

/**
 * Wro4j Manager Factory. Optimized for built-in usage

 * @author avarabyeu
 */
internal class WroManagerFactory : ConfigurableWroManagerFactory() {

    override fun newProcessorsFactory(): ProcessorsFactory {
        val factory = SimpleProcessorsFactory()
        factory.addPreProcessor(object : ProcessorDecorator(CoffeeScriptProcessor()) {
            @Throws(IOException::class)
            override fun process(resource: Resource, reader: Reader, writer: Writer) {
                if (resource.uri.endsWith(COFFEE_FILENAME)) {
                    super.process(resource, reader, writer)
                } else {
                    CharStreams.copy(reader, writer)
                }
            }
        })
        factory.addPreProcessor(CssUrlRewritingProcessor())
        factory.addPostProcessor(RubySassCssProcessor())

        return factory
    }

    override fun newModelFactory(): WroModelFactory {
        return object : XmlModelFactory() {
            @Throws(IOException::class)
            override fun getModelResourceAsStream(): InputStream {
                return Resources.asByteSource(getResource("wro.xml")).openStream()
            }
        }
    }

    override fun newConfigProperties(): Properties {
        val properties = Properties()
        try {
            Resources.asByteSource(Resources.getResource("wro.properties")).openStream().use { `is` -> properties.load(`is`) }
        } catch (e: IOException) {
            throw IncorrectConfigurationException("Unable to load wro configuration")
        }

        return properties
    }

    companion object {
        private val COFFEE_FILENAME = ".coffee"
    }

}

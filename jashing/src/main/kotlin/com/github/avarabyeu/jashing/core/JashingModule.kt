package com.github.avarabyeu.jashing.core

import com.github.avarabyeu.jashing.utils.ResourceUtils
import com.google.common.eventbus.EventBus
import com.google.common.io.Resources
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.Service
import com.google.gson.Gson
import com.google.inject.AbstractModule
import com.google.inject.Module
import com.google.inject.Provider
import com.google.inject.Scopes
import com.google.inject.name.Names
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * Main application configuration module. Configures server and all necessary stuff

 * @author avarabyeu
 */
internal class JashingModule(private val port: Int?, private val extensions: List<Module>) : AbstractModule() {

    constructor(extensions: List<Module>) : this(null, extensions) {
    }

    override fun configure() {

        binder().requireExplicitBindings()

        /* Event Bus. In charge of dispatching events from message sources to event handlers */
        val eventBus = EventBus { exception, context ->
            LOGGER.error("Could not dispatch event: {} to {}",
                    context.subscriber, context.subscriberMethod,
                    exception.cause)
        }

        binder().bind(EventBus::class.java).toInstance(eventBus)

        val gson = Gson()
        binder().bind(Gson::class.java).toInstance(gson)

        /* binds properties. Replaces property files with json-based configuration. Just to have all events-related properties in one file */
        val configuration = provideConfiguration(gson)
        val globalProperties = configuration.properties
        globalProperties!!.entries.forEach { entry ->
            binder().bindConstant()
                    .annotatedWith(Names.named(entry.key)).to(entry.value)
        }


        /* install module with events configuration */
        binder().install(EventsModule(configuration.events!!, extensions))

        binder().bind(JashingServer::class.java).toProvider(Provider {
            val jashing = JashingServer(this.port ?: DEFAULT_PORT, eventBus, gson,
                    java.lang.Long.valueOf(globalProperties[TIMEOUT_PROPERTY]))
            jashing.addListener(object : Service.Listener() {
                override fun running() {
                    LOGGER.info("Embedded Jashing server has started on port [{}]", port)
                }

                override fun stopping(from: Service.State?) {
                    LOGGER.info("Stopping embedded Jashing server")
                }
            }, MoreExecutors.directExecutor())
            jashing
        }).`in`(Scopes.SINGLETON)

    }

    private fun provideConfiguration(gson: Gson): Configuration {
        try {
            val config = ResourceUtils.getResourceAsURL(APPLICATION_CONFIG)
            return gson.fromJson(Resources.asCharSource(config, com.google.common.base.Charsets.UTF_8).openBufferedStream(), Configuration::class.java)
        } catch (e: IOException) {
            throw IllegalStateException("Unable to read configuration", e)

        }

    }

    companion object {
        private val DEFAULT_PORT = 8181
        private val LOGGER = LoggerFactory.getLogger(JashingModule::class.java)
        private val APPLICATION_CONFIG = "config.json"
        private val TIMEOUT_PROPERTY = "timeout"
    }

}

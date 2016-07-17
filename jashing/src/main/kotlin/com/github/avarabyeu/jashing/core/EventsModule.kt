package com.github.avarabyeu.jashing.core

import com.github.avarabyeu.jashing.core.eventsource.annotation.EventId
import com.github.avarabyeu.jashing.core.eventsource.annotation.Frequency
import com.github.avarabyeu.jashing.utils.InstanceOfMap
import com.google.common.annotations.VisibleForTesting
import com.google.common.base.Joiner
import com.google.common.base.Preconditions
import com.google.common.base.Strings
import com.google.common.reflect.ClassPath
import com.google.common.util.concurrent.Service
import com.google.common.util.concurrent.ServiceManager
import com.google.inject.*
import com.google.inject.multibindings.Multibinder
import com.google.inject.multibindings.OptionalBinder
import com.google.inject.name.Names
import com.google.inject.spi.Message
import org.slf4j.LoggerFactory
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.time.Duration
import java.util.*

/**
 * Guice configuration [com.google.inject.Module] for event handlers. Create all defined in configuration event handlers,
 * populates them with needed values and registers as services

 * @author avarabyeu
 */
internal class EventsModule @JvmOverloads constructor(eventConfigs: List<Configuration.EventConfig>, extensions: List<Module>? = null) : PrivateModule() {


    private val eventConfigs: List<Configuration.EventConfig>

    private val extensionsMap: InstanceOfMap<Module>

    init {
        this.eventConfigs = Preconditions.checkNotNull(eventConfigs, "Event configs shouldn't be null")
        this.extensionsMap = if (null == extensions) InstanceOfMap.empty<Module>() else InstanceOfMap.builder<Module>().fromList(extensions)
    }

    override fun configure() {
        try {

            this.extensionsMap.values.forEach { this.install(it) }

            val eventSources = mapEventSources()

            val eventSourceMultibinder = Multibinder.newSetBinder(binder(), com.google.common.util.concurrent.Service::class.java)

            for (event in eventConfigs) {
                if (Strings.isNullOrEmpty(event.source)) {
                    binder().addError("Event source is not specified for event with id '%s'", event.id)
                    continue
                }

                if (!eventSources.containsKey(event.source)) {
                    binder().addError("Unable to find event source with name '%s'. Available sources: \n%s", event.source, Joiner.on('\n').join(eventSources.keys))
                } else {
                    install(EventSourcePrivateModule(event, eventSourceMultibinder, eventSources.get(event.source)!!))

                }
            }

        } catch (e: IOException) {
            addError(Message("Unable to load event handlers...", e))
        }

    }

    @Provides
    @Singleton
    @Exposed
    fun serviceManager(eventSources: java.util.Set<com.google.common.util.concurrent.Service>): ServiceManager {
        val serviceManager = ServiceManager(eventSources)
        serviceManager.addListener(object : ServiceManager.Listener() {
            override fun healthy() {
                LOGGER.info("Event sources have bootstrapped!")
            }

            override fun stopped() {
                LOGGER.info("Event sources have stopped")
            }
        })
        return serviceManager
    }


    /**
     * Scans whole application classpath and finds events sources

     * @return 'event source name' -> 'event handler class' map
     * *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @VisibleForTesting
    @Throws(IOException::class)
    fun mapEventSources(): Map<String, Class<out Service>> {

        /** Obtains all classpath's top level classes  */
        val classes = ClassPath.from(Thread.currentThread().contextClassLoader).allClasses
        LOGGER.info("Scanning classpath for EventHandlers....")

        val collected = classes
                .map {
                    try {
                        Optional.ofNullable(it.load())
                    } catch (e: NoClassDefFoundError) {
                        Optional.empty<Class<Any>>()
                    }
                }
                .filter { it.isPresent }
                .map { it.get() }
                .filter { it.isAnnotationPresent(EventSource::class.java) }
                .filter { Service::class.java.isAssignableFrom(it) }
                .associateBy({ it.getAnnotation(EventSource::class.java).value }, { it as Class<out com.google.common.util.concurrent.Service> })



        LOGGER.info("Found {} event handlers", collected.size)
        return collected
    }


    /**
     * [com.google.inject.PrivateModule] binds event source type to some particular event and exposes it to parent module
     */
    private inner class EventSourcePrivateModule(private val event: Configuration.EventConfig, private val multibinder: Multibinder<Service>, private val eventSourceClass: Class<out Service>) : PrivateModule() {

        override fun configure() {
            LOGGER.info("Registering event source [{}] for event [{}]", eventSourceClass.simpleName, event.id)
            validateEvent()

            binder().bind(Duration::class.java).annotatedWith(Frequency::class.java).toInstance(Duration.ofSeconds(event.frequency))
            binder().bind(String::class.java).annotatedWith(EventId::class.java).toInstance(event.id)

            val eventSourceKey = Key.get(com.google.common.util.concurrent.Service::class.java, Names.named(event.id!!))
            binder().bind(eventSourceKey).to(eventSourceClass)

            expose(eventSourceKey)

            multibinder.addBinding().to(eventSourceKey)

            if (null != event.properties) {
                event.properties!!.entries.forEach { entry -> bindProperty(entry.key, entry.value) }
            }

            /* each event handler may have own explicit guice configuration. Install it if so */
            if (NOP::class.java != eventSourceClass.getAnnotation(EventSource::class.java).explicitConfiguration) {
                LOGGER.info("       Registering extension module for event source [{}]", eventSourceClass.simpleName)
                val extensionModuleClass = eventSourceClass.getAnnotation(EventSource::class.java).explicitConfiguration
                val extensionModule = extensionsMap.getInstanceOf(extensionModuleClass.java)

                /* if extension module is not provided explicitly, let's create it in private module scope
                 *  otherwise it should be already installed in EventsModule
                 */
                if (null == extensionModule) {
                    try {
                        install(extensionModuleClass.constructors.first().call())
                    } catch (e: InvocationTargetException) {
                        LOGGER.error("Unable to initialize extension", e)
                        addError("Unable to create instance of extension module '%s' for event with ID '%s'. Exception '%s'", extensionModuleClass, event.id, e.message)
                    } catch (e: InstantiationException) {
                        LOGGER.error("Unable to initialize extension", e)
                        addError("Unable to create instance of extension module '%s' for event with ID '%s'. Exception '%s'", extensionModuleClass, event.id, e.message)
                    } catch (e: NoSuchMethodException) {
                        LOGGER.error("Unable to initialize extension", e)
                        addError("Unable to create instance of extension module '%s' for event with ID '%s'. " + "Look like it doesn't have default constructor. Please, register it explicitly", extensionModuleClass, event.id)
                    } catch (e: IllegalAccessException) {
                        LOGGER.error("Unable to initialize extension", e)
                        addError("Unable to create instance of extension module '%s' for event with ID '%s'. " + "Look like it doesn't have default constructor. Please, register it explicitly", extensionModuleClass, event.id)
                    }

                }

            }

        }

        @SuppressWarnings("unchecked")
        private fun <T> bindProperty(key: String, value: T) where T : Any {
            val type = TypeLiteral.get(value.javaClass)
            /* make possible to use Optional<T> injections */
            OptionalBinder
                    .newOptionalBinder(binder(), Key.get(type, Names.named(key)))
                    .setBinding()
                    .toInstance(value)
        }

        private fun validateEvent() {
            if (event.frequency <= 0) {
                binder().addError("Frequency of event with ID '%s' is not specified", event.id)
            }

            if (null == event.id) {
                binder().addError("ID of event with ID '%s' is not specified", event.id)
            }
        }
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(EventsModule::class.java)

        class NOP : AbstractModule() {
            override fun configure() {
                //do nothing
            }
        }
    }


}

package com.github.avarabyeu.jashing.core

import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ServiceManager
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Application Entry Point. Creates Guava's Injector and runs spark server

 * @author avarabyeu
 */
class Jashing private constructor(private val injector: Injector) {

    private val bootstrapped: AtomicBoolean
    private val shutdownHook: Thread

    init {
        this.bootstrapped = AtomicBoolean(false)
        this.shutdownHook = Thread(Runnable { this.shutdown() })
    }

    /**
     * Bootstaps Jashing. This operation is allowed only once. Bootstrapping already started Jashing is not permitted

     * @return yourself
     */
    fun bootstrap(): Jashing {
        if (bootstrapped.compareAndSet(false, true)) {

            /* bootstrap event sources* */
            val eventSources = injector.getInstance(ServiceManager::class.java)
            eventSources.startAsync()

            /* bootstrap server */
            val application = injector.getInstance(JashingServer::class.java)
            application.startAsync()

            Runtime.getRuntime().addShutdownHook(shutdownHook)
            LOGGER.info("Jashing has started!")
        } else {
            throw IllegalStateException("Jashing already bootstrapped")
        }
        return this
    }

    /**
     * Shutdowns Jashing. Permitted only for bootstrapped instance
     */
    fun shutdown() {
        if (bootstrapped.compareAndSet(true, false)) {
            LOGGER.info("Shutting down Jashing...")

            injector.getInstance(ServiceManager::class.java).stopAsync().awaitStopped()
            injector.getInstance(JashingServer::class.java).stopAsync().awaitTerminated()


            /* shutdown method might be called by this hook. So, trying to remove
             * hook which is currently is progress causes error
             */
            if (!shutdownHook.isAlive) {
                Runtime.getRuntime().removeShutdownHook(shutdownHook)
            }
            LOGGER.info("Jashing has stopped.")
        } else {
            throw IllegalStateException("Jashing is not bootstrapped")
        }
    }

    /**
     * Jashing builder
     */
    class Builder {

        /* List of extension-modules */
        private val modules = LinkedList<Module>()

        private var port: Int? = null

        /**
         * Port for embedded mode.

         * @param port port Jashing should start on
         * *
         * @return this builder
         */
        fun withPort(port: Int): Builder {
            Preconditions.checkArgument(0 < port, "Port incorrect port number %s", port)
            this.port = port
            return this
        }

        /**
         * Registers extension modules

         * @param modules Array of extension modules
         * *
         * @return this builder
         */
        fun registerModule(vararg modules: Module): Builder {
            Collections.addAll(this.modules, *modules)
            return this
        }

        /**
         * Builds Jashing instance

         * @return Jashing
         */
        fun build(): Jashing {
            val createdInjector = Guice.createInjector(JashingModule(port, ImmutableList.builder<Module>().addAll(modules).build()))

            return Jashing(createdInjector)
        }
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(Jashing::class.java)

        /**
         * Creates new Jashing builder

         * @return Builder of Jashing instance
         */
        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
        
    }

}

package com.github.avarabyeu.jashing.integration.vcs

import com.github.avarabyeu.jashing.core.IncorrectConfigurationException
import com.github.avarabyeu.jashing.utils.ResourceUtils
import com.google.common.io.Resources
import com.google.gson.Gson
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.multibindings.Multibinder
import java.io.IOException
import javax.inject.Provider

/**
 * VCS Configuration module

 * @author Andrei Varabyeu
 */
abstract class AbstractVcsModule : AbstractModule() {

    override fun configure() {
        val multibinder = Multibinder.newSetBinder(binder(), VCSClient::class.java)
        clients().forEach { multibinder.addBinding().toProvider(Provider { it }) }
    }

    /**
     * Creates composite VCS clients from another client in context

     * @param clients Client delegates from context
     * *
     * @return Composite client
     */
    @Provides
    @Singleton
    fun vcsClient(clients: Set<VCSClient>): VCSClient {
        return CompositeVCSClient(clients)
    }

    /**
     * Loads VCS configuration

     * @param gson GSON for deserialization
     * *
     * @return Loaded configuration
     */
    @Provides
    fun loadConfiguration(gson: Gson): VCSConfiguration {
        try {
            val resource = ResourceUtils.getResourceAsURL(VCS_CONFIG_JSON)
            return gson.fromJson(Resources.asCharSource(resource, com.google.common.base.Charsets.UTF_8).openStream(), VCSConfiguration::class.java)
        } catch (e: IOException) {
            throw IncorrectConfigurationException("Unable to read VCS configuration", e)
        }

    }

    /**
     * @return Set of VCS clients
     */
    internal abstract fun clients(): List<VCSClient>

    companion object {

        val VCS_CONFIG_JSON = "vcs-config.json"
    }
}

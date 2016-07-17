package com.github.avarabyeu.jashing.integration.vcs

import com.google.common.collect.ImmutableSet

/**
 * Composes different types of CVS modules like SVN Module + GIT module

 * @author Andrei Varabyeu
 */
class CompositeVcsModule(vararg moduleArray: AbstractVcsModule) : AbstractVcsModule() {

    private val modules: Set<AbstractVcsModule>

    init {
        modules = ImmutableSet.copyOf(moduleArray)
    }


    override fun clients(): List<VCSClient> {
        return modules.flatMap { it.clients() }
    }


}

package com.github.avarabyeu.jashing.integration.vcs;

import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Composes different types of CVS modules like SVN Module + GIT module
 *
 * @author Andrei Varabyeu
 */
public class CompositeVcsModule extends AbstractVcsModule {

    private Set<AbstractVcsModule> modules;

    public CompositeVcsModule(AbstractVcsModule... moduleArray) {
        modules = ImmutableSet.copyOf(moduleArray);
    }


    @Override
    protected List<VCSClient> getClients() {
        return modules.stream().flatMap(module -> module.getClients().stream()).collect(Collectors.toList());
    }


}

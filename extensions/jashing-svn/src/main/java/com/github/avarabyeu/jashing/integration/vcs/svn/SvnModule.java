package com.github.avarabyeu.jashing.integration.vcs.svn;

import com.github.avarabyeu.jashing.integration.vcs.AbstractVcsModule;
import com.github.avarabyeu.jashing.integration.vcs.VCSClient;
import com.github.avarabyeu.jashing.integration.vcs.VCSConfiguration;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andrei Varabyeu
 */
public class SvnModule extends AbstractVcsModule {

    @Override
    protected List<VCSClient> getClients() {
        VCSConfiguration vcsConfig = loadConfiguration(new Gson());
        return vcsConfig.getSvns().stream().map(config -> new SvnClient(config.getUrl(), config.getUsername(), config.getPassword())).collect(Collectors.toList());
    }
}

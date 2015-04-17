package com.github.avarabyeu.jashing.integration.vcs.git;

import com.github.avarabyeu.jashing.integration.vcs.AbstractVcsModule;
import com.github.avarabyeu.jashing.integration.vcs.VCSClient;
import com.github.avarabyeu.jashing.integration.vcs.VCSConfiguration;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GIT configuration module
 *
 * @author Andrei Varabyeu
 */
public class GitModule extends AbstractVcsModule {

    @Override
    protected List<VCSClient> getClients() {
        VCSConfiguration vcsConfig = loadConfiguration(new Gson());
        return vcsConfig.getGits().stream().map(config -> new GitClient(config.getUrl(), config.getRepoName(), config.getBranches()))
                .collect(Collectors.toList());
    }
}

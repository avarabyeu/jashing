package com.github.avarabyeu.jashing.integration.vcs;

import com.github.avarabyeu.jashing.core.IncorrectConfigurationException;
import com.github.avarabyeu.jashing.integration.vcs.git.GitClient;
import com.github.avarabyeu.jashing.integration.vcs.svn.SvnClient;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * VCS Configuration module
 *
 * @author Andrei Varabyeu
 */
public class VcsModule extends AbstractModule {


    @Override
    protected void configure() {
        Multibinder<VCSClient> multibinder = Multibinder.newSetBinder(binder(), VCSClient.class);

        VCSConfiguration vcsConfig = loadConfiguration(new Gson());
        vcsConfig.getSvns().stream().
                forEach(config -> multibinder.addBinding()
                        .toProvider(() -> new SvnClient(config.getUrl(), config.getUsername(), config.getPassword())));

        vcsConfig.getGits().stream().forEach(config -> multibinder.addBinding()
                .toProvider(() -> new GitClient(config.getUrl(), config.getRepoName())));
    }

    @Provides
    @Singleton
    public VCSClient vcsClient(Set<VCSClient> clients) {
        return new CompositeVCSClient(clients);
    }


    @Provides
    public VCSConfiguration loadConfiguration(Gson gson) {
        try {
            URL resource = Thread.currentThread().getContextClassLoader().getResource("vcs-config.json");
            if (null == resource) {
                throw new IncorrectConfigurationException("Unable to find VCS configuration");
            }
            return gson.fromJson(Resources.asCharSource(resource, Charsets.UTF_8).openStream(),
                    VCSConfiguration.class);
        } catch (IOException e) {
            throw new IncorrectConfigurationException("Unable to read VCS configuration", e);
        }
    }
}

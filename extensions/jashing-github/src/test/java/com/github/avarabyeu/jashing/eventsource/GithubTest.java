package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.integration.vcs.github.GitHubClient;
import com.github.avarabyeu.jashing.integration.vcs.github.GithubModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Andrei Varabyeu
 */
public class GithubTest {


    @Test
    @Ignore
    public void testGithub() {
        Injector injector = Guice.createInjector(new GithubModule());
        GitHubClient gitClient = injector.getInstance(GitHubClient.class);

        System.out.println(gitClient.getUserRepositories("perwendel"));

    }
}

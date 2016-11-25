package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.integration.github.Filters;
import com.github.avarabyeu.jashing.integration.github.GitHubClient;
import com.github.avarabyeu.jashing.integration.github.GithubModule;
import com.github.avarabyeu.jashing.integration.github.model.PullRequest;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Andrei Varabyeu
 */
public class GithubTest {

    @Test
    @Ignore
    public void testGithub() throws IOException {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                Map<String, String> props = new HashMap<>();
                props.put("github.oauth.token", "сhange-me");
                props.put("github.organization", "reportportal");

                Names.bindProperties(binder(), props);
            }
        }, new GithubModule());

        GitHubClient gitHub = injector.getInstance(GitHubClient.class);

        String orgName = "reportportal";
        final List<PullRequest> openedRequests = gitHub.getOpenedIssues(orgName)
                .stream()
                .filter(Filters.IS_PULL_REQUEST)
                .map(issue -> {
                    System.out.println("ID:" + StringUtils.substringAfterLast(issue.getPullRequest().getUrl(), "/"));
                    return issue;
                })
                .map(issue -> gitHub.getPullRequest(orgName, issue.getRepository().getName(),
                        /* parse PR id */
                        StringUtils
                                .substringAfterLast(StringUtils.stripEnd(issue.getPullRequest().getUrl(), "/"), "/")))
                .collect(Collectors.toList());
        System.out.println(openedRequests);

    }
}

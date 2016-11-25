package com.github.avarabyeu.jashing.integration.github;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.ListEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = "github-issues-per-project-source", explicitConfiguration = GithubModule.class)
public class GithubIssuesPerProjectSource extends ScheduledEventSource<ListEvent<Long>> {

    private final GitHubClient gitHub;
    private final String orgName;

    @Inject
    public GithubIssuesPerProjectSource(GitHubClient gitHub, @Named("github.organization") String orgName) {
        this.gitHub = gitHub;
        this.orgName = orgName;
    }

    @Override
    protected ListEvent<Long> produceEvent() {

        final Map<String, Long> issuesPerRepo = gitHub.getOpenedIssues(orgName)
                .stream()
                .filter(Filters.IS_ISSUE)
                .collect(Collectors
                        .groupingBy(issue -> issue.getRepository().getName(), counting()));

        return new ListEvent<>(issuesPerRepo.entrySet()
                .stream()
                .map(entry -> new ListEvent.Item<>(entry.getKey(), entry.getValue()))
                .sorted(comparing(ListEvent.Item::getValue))
                .collect(toList()));
    }
}

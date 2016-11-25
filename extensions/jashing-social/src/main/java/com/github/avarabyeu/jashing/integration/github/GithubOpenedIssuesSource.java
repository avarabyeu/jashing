package com.github.avarabyeu.jashing.integration.github;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.NumberEvent;
import com.github.avarabyeu.jashing.integration.github.model.Issue;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = "github-opened-issues-source", explicitConfiguration = GithubModule.class)
public class GithubOpenedIssuesSource extends ScheduledEventSource<NumberEvent> {

    private final GitHubClient gitHub;
    private final String orgName;

    @Inject
    public GithubOpenedIssuesSource(GitHubClient gitHub, @Named("github.organization") String orgName) {
        this.gitHub = gitHub;
        this.orgName = orgName;
    }

    @Override
    protected NumberEvent produceEvent() {
        final List<Issue> openedIssues = gitHub.getOpenedIssues(orgName).stream().filter(Filters.IS_ISSUE).collect(
                Collectors.toList());
        int currentSize = openedIssues.size();
        Long wasYesterday = openedIssues.stream()
                .filter(i -> i.getCreatedAt().isBefore(LocalDateTime.now().toLocalDate().atStartOfDay())).count();
        return new NumberEvent(currentSize, wasYesterday.intValue());

    }
}

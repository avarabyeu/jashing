package com.github.avarabyeu.jashing.integration.github;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.StatusAwareNumberEvent;
import com.github.avarabyeu.jashing.integration.github.model.Issue;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = "github-pending-issues-source", explicitConfiguration = GithubModule.class)
public class PendingIssuesSource extends ScheduledEventSource<StatusAwareNumberEvent> {

    private final GitHubClient gitHub;
    private final String orgName;

    @Inject
    public PendingIssuesSource(GitHubClient gitHub, @Named("github.organization") String orgName) {
        this.gitHub = gitHub;
        this.orgName = orgName;
    }

    @Override
    protected StatusAwareNumberEvent produceEvent() {
        final List<Issue> openedIssues = gitHub.getOpenedIssues(orgName)
                .stream()
                .filter(Filters.IS_ISSUE)
                .filter(i -> i.getAssignees().isEmpty() && i.getLabels().isEmpty()).collect(toList());

        int currentSize = openedIssues.size();
        Long wasYesterday = openedIssues.stream()
                .filter(i -> i.getCreatedAt().isBefore(LocalDateTime.now().toLocalDate().atStartOfDay())).count();
        StatusAwareNumberEvent.Status status = currentSize > 0 ? StatusAwareNumberEvent.Status.warning :
                StatusAwareNumberEvent.Status.ok;
        return new StatusAwareNumberEvent(status, currentSize, wasYesterday.intValue());

    }
}

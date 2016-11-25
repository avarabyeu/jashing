package com.github.avarabyeu.jashing.integration.github;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.StatusAwareNumberEvent;
import com.github.avarabyeu.jashing.integration.github.model.PullRequest;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.stripEnd;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = "github-pending-prs-source", explicitConfiguration = GithubModule.class)
public class PendingPullRequestsSource extends ScheduledEventSource<StatusAwareNumberEvent> {

    private final GitHubClient gitHub;
    private final String orgName;

    @Inject
    public PendingPullRequestsSource(GitHubClient gitHub, @Named("github.organization") String orgName) {
        this.gitHub = gitHub;
        this.orgName = orgName;
    }

    @Override
    public StatusAwareNumberEvent produceEvent() {
        final List<PullRequest> openedRequests = gitHub.getOpenedIssues(orgName)
                .stream()
                .filter(Filters.IS_PULL_REQUEST)
                .map(issue -> gitHub.getPullRequest(orgName, issue.getRepository().getName(),
                        /* parse PR id */
                        substringAfterLast(stripEnd(issue.getPullRequest().getUrl(), "/"), "/")))
                .collect(toList());

        int currentSize = openedRequests.size();
        Long wasYesterday = openedRequests.stream()
                .filter(r -> r.getCreatedAt().isBefore(LocalDateTime.now().toLocalDate().atStartOfDay())).count();

        StatusAwareNumberEvent.Status status;
        if ((currentSize - wasYesterday) > 0) {
            status = StatusAwareNumberEvent.Status.warning;
        } else if (currentSize > 0) {
            status = StatusAwareNumberEvent.Status.danger;
        } else {
            status = StatusAwareNumberEvent.Status.ok;
        }

        return new StatusAwareNumberEvent(status, currentSize, wasYesterday.intValue());

    }
}

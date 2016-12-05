package com.github.avarabyeu.jashing.integration.github

import com.github.avarabyeu.jashing.core.EventSource
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource
import com.github.avarabyeu.jashing.events.ListEvent
import com.github.avarabyeu.jashing.events.NumberEvent
import com.github.avarabyeu.jashing.events.StatusAwareNumberEvent
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

val IS_PULL_REQUEST: (Issue) -> Boolean = { issue -> null != issue.pullRequest }
val IS_ISSUE = { issue: Issue -> !IS_PULL_REQUEST.invoke(issue) }

@EventSource(value = "github-issues-per-project-source", explicitConfiguration = GithubModule::class)
class GithubIssuesPerProjectSource @Inject constructor(gitHub: GitHubClient, @Named("github.organization") orgName: String) :
        ScheduledEventSource<ListEvent<Long>>() {

    val gitHub: GitHubClient
    val orgName: String

    init {
        this.gitHub = gitHub
        this.orgName = orgName
    }

    override fun produceEvent(): ListEvent<Long> {
        val issuesPerRepo = gitHub.getOpenedIssues(orgName)
                .filter(IS_ISSUE)
                .groupBy { issue -> issue.repository.name }.mapValues { e -> e.value.size.toLong() }

        val sorted = issuesPerRepo
                .map { e -> ListEvent.Item(e.key, e.value) }
                .sortedBy { item -> item.value }

        return ListEvent(sorted)
    }
}

@EventSource(value = "github-opened-issues-source", explicitConfiguration = GithubModule::class)
class GithubOpenedIssuesSource @Inject constructor(gitHub: GitHubClient, @Named("github.organization") orgName: String) :
        ScheduledEventSource<NumberEvent>() {

    val gitHub: GitHubClient
    val orgName: String

    init {
        this.gitHub = gitHub
        this.orgName = orgName
    }

    override fun produceEvent(): NumberEvent {
        val openedIssues = gitHub.getOpenedIssues(orgName)
                .filter(IS_ISSUE)

        val currentSize = openedIssues.size
        val wasYesterday = openedIssues.count { it.createdAt.isBefore(LocalDateTime.now().toLocalDate().atStartOfDay()) }
        return NumberEvent(currentSize, wasYesterday)
    }
}

@EventSource(value = "github-pending-issues-source", explicitConfiguration = GithubModule::class)
class PendingIssuesSource @Inject constructor(gitHub: GitHubClient, @Named("github.organization") orgName: String) :
        ScheduledEventSource<StatusAwareNumberEvent>() {

    val gitHub: GitHubClient
    val orgName: String

    init {
        this.gitHub = gitHub
        this.orgName = orgName
    }

    override fun produceEvent(): StatusAwareNumberEvent {
        val openedIssues = gitHub.getOpenedIssues(orgName)
                .filter(IS_ISSUE)
                .filter { it.assignees.isEmpty() && it.labels.isEmpty() }

        val currentSize = openedIssues.size
        val wasYesterday = openedIssues.count { i -> i.createdAt.isBefore(LocalDateTime.now().toLocalDate().atStartOfDay()) }
        val status = if (currentSize > 0)
            StatusAwareNumberEvent.Status.warning
        else
            StatusAwareNumberEvent.Status.ok
        return StatusAwareNumberEvent(status, currentSize, wasYesterday)
    }
}

@EventSource(value = "github-pending-prs-source", explicitConfiguration = GithubModule::class)
class PendingPullRequestsSource
@Inject constructor(gitHub: GitHubClient, @Named("github.organization") orgName: String) :
        ScheduledEventSource<StatusAwareNumberEvent>() {

    val gitHub: GitHubClient
    val orgName: String

    init {
        this.gitHub = gitHub
        this.orgName = orgName
    }

    override fun produceEvent(): StatusAwareNumberEvent {
        val openedRequests = gitHub.getOpenedIssues(orgName)
                .filter(IS_PULL_REQUEST)
                .map { issue ->
                    gitHub.getPullRequest(orgName, issue.repository.name,
                            /* parse PR id */
                            issue.pullRequest!!.url.trimEnd { '/' == it }.substringAfterLast('/'))
                }

        val currentSize = openedRequests.size
        val wasYesterday = openedRequests
                .filter({ r -> r.createdAt.isBefore(LocalDateTime.now().toLocalDate().atStartOfDay()) }).count()

        val status: StatusAwareNumberEvent.Status
        if (currentSize - wasYesterday > 0) {
            status = StatusAwareNumberEvent.Status.warning
        } else if (currentSize > 0) {
            status = StatusAwareNumberEvent.Status.danger
        } else {
            status = StatusAwareNumberEvent.Status.ok
        }

        return StatusAwareNumberEvent(status, currentSize, wasYesterday)

    }
}


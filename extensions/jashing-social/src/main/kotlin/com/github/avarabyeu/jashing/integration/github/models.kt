package com.github.avarabyeu.jashing.integration.github

import java.time.LocalDateTime
import java.util.*


data class Milestone(val title: String, val createdAt: Date?,
                     val dueOn: Date?, val closedIssues: Int?, val number: Int?, val openIssues: Int?,
                     val description: String?, val state: String?, val url: String?, val creator: User?)

data class Issue(val repository: Repository, var createdAt: LocalDateTime,
                 val labels: List<Label>, val assignees: List<User>, val pullRequest: PullRequest?)

data class Label(val id: Long, val name: String, val url: String, val color: String?, val default: Boolean)

data class PullRequestInfo(
        val url: String,
        val htmlUrl: String?,
        val diffUrl: String?,
        val patchUrl: String?
)

data class PullRequest(
        val id: Long,
        val user: User,
        val state: String,
        val title: String,
        val url: String,
        val mergeable: Boolean?,
        val merged: Boolean?,
        val closedAt: LocalDateTime?,
        val mergedAt: LocalDateTime?,
        val updatedAt: LocalDateTime?,
        val createdAt: LocalDateTime,
        val additions: Int?,
        val changedFiles: Int,
        val comments: Int?,
        val reviewComments: Int?,
        val commits: Int?,
        val deletions: Int?,
        val number: Int?,
        val milestone: Milestone?,
        val base: PullRequestMarker?,
        val head: PullRequestMarker?,
        val body: String?,
        val bodyHtml: String?,
        val bodyText: String?,
        val diffUrl: String?,
        val htmlUrl: String?,
        val issueUrl: String?,
        val patchUrl: String?,
        val assignee: User?,
        val mergedBy: User?
)

data class PullRequestMarker(
        val sha: String,
        val repo: Repository,
        val label: String?,
        val ref: String?,
        val user: User?
)

data class Repository(
        val id: Long,
        val name: String,
        val url: String,
        val sshUrl: String,
        val svnUrl: String?,
        val cloneUrl: String,
        val homepage: String?,
        val gitUrl: String,
        val htmlUrl: String?,
        val mirrorUrl: String?,
        val language: String?,
        val owner: User,
        val description: String? = null,
        val fork: Boolean = false,
        val hasDownloads: Boolean = false,
        val hasIssues: Boolean = false,
        val hasWiki: Boolean = false,
        val isPrivate: Boolean,
        val createdAt: Date?,
        val pushedAt: Date?,
        val updatedAt: Date?,
        val forks: Int?,
        val openIssues: Int = 0,
        val size: Int?,
        val watchers: Int = 0,
        val parent: Repository?,
        val source: Repository?,
        val defaultBranch: String?
)


data class User(
        val id: Int,
        val login: String,
        val name: String,
        val type: String,
        val url: String,
        val plan: UserPlan?,
        val hireable: Boolean = false,
        val createdAt: Date,
        val collaborators: Int = 0,
        val diskUsage: Int?,
        val followers: Int = 0,
        val following: Int = 0,
        val ownedPrivateRepos: Int = 0,
        val privateGists: Int = 0,
        val publicGists: Int = 0,
        val publicRepos: Int = 0,
        val totalPrivateRepos: Int = 0,
        val avatarUrl: String?,
        val bio: String?,
        val blog: String?,
        val company: String?,
        val email: String?,
        val gravatarId: String?,
        val htmlUrl: String?,
        val location: String?
)


data class UserPlan(
        val collaborators: Long = 0,
        val privateRepos: Long = 0,
        val space: Long = 0,
        val name: String? = null
)

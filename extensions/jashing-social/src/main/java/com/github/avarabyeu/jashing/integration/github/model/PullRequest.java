/**
 * ****************************************************************************
 * Copyright (c) 2011 GitHub Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Kevin Sawicki (GitHub Inc.) - initial API and implementation
 * *****************************************************************************
 */

package com.github.avarabyeu.jashing.integration.github.model;

import java.time.LocalDateTime;

/**
 * Pull request model class.
 * @author Andrei Varabyeu
 */
public class PullRequest {

    private boolean mergeable;

    private boolean merged;

    private LocalDateTime closedAt;

    private LocalDateTime mergedAt;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private long id;

    private int additions;

    private int changedFiles;

    private int comments;

    private int reviewComments;

    private int commits;

    private int deletions;

    private int number;

    private Milestone milestone;

    private PullRequestMarker base;

    private PullRequestMarker head;

    private String body;

    private String bodyHtml;

    private String bodyText;

    private String diffUrl;

    private String htmlUrl;

    private String issueUrl;

    private String patchUrl;

    private String state;

    private String title;

    private String url;

    private User assignee;

    private User mergedBy;

    private User user;

    /**
     * @return mergeable
     */
    public boolean isMergeable() {
        return mergeable;
    }

    /**
     * @param mergeable Mergeable
     * @return this pull request
     */
    public PullRequest setMergeable(boolean mergeable) {
        this.mergeable = mergeable;
        return this;
    }

    /**
     * @return merged
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     * @param merged Merged
     * @return this pull request
     */
    public PullRequest setMerged(boolean merged) {
        this.merged = merged;
        return this;
    }

    /**
     * @return closedAt
     */
    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    /**
     * @param closedAt Closed At
     * @return this pull request
     */
    public PullRequest setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
        return this;
    }

    /**
     * @return mergedAt
     */
    public LocalDateTime getMergedAt() {
        return mergedAt;
    }

    /**
     * @param mergedAt Merged At
     * @return this pull request
     */
    public PullRequest setMergedAt(LocalDateTime mergedAt) {
        this.mergedAt = mergedAt;
        return this;
    }

    /**
     * @return updatedAt
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt Updated at
     * @return this pull request
     */
    public PullRequest setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * @return createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt Created At
     * @return this pull request
     */
    public PullRequest setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @return additions
     */
    public int getAdditions() {
        return additions;
    }

    /**
     * @param additions Additions
     * @return this pull request
     */
    public PullRequest setAdditions(int additions) {
        this.additions = additions;
        return this;
    }

    /**
     * @return changedFiles
     */
    public int getChangedFiles() {
        return changedFiles;
    }

    /**
     * @param changedFiles Changed files
     * @return this pull request
     */
    public PullRequest setChangedFiles(int changedFiles) {
        this.changedFiles = changedFiles;
        return this;
    }

    /**
     * @return comments
     */
    public int getComments() {
        return comments;
    }

    /**
     * @param comments Comments
     * @return this pull request
     */
    public PullRequest setComments(int comments) {
        this.comments = comments;
        return this;
    }

    /**
     * @return number of inline comments on the diff in the pull request
     */
    public int getReviewComments() {
        return reviewComments;
    }

    /**
     * @param reviewComments {@link #getReviewComments()}
     * @return this pull request
     */
    public PullRequest setReviewComments(int reviewComments) {
        this.reviewComments = reviewComments;
        return this;
    }

    /**
     * @return commits
     */
    public int getCommits() {
        return commits;
    }

    /**
     * @param commits Commits
     * @return this pull request
     */
    public PullRequest setCommits(int commits) {
        this.commits = commits;
        return this;
    }

    /**
     * @return deletions
     */
    public int getDeletions() {
        return deletions;
    }

    /**
     * @param deletions Deletions
     * @return this pull request
     */
    public PullRequest setDeletions(int deletions) {
        this.deletions = deletions;
        return this;
    }

    /**
     * @return number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number Number
     * @return this pull request
     */
    public PullRequest setNumber(int number) {
        this.number = number;
        return this;
    }

    /**
     * @return base
     */
    public PullRequestMarker getBase() {
        return base;
    }

    /**
     * @param base Base
     * @return this pull request
     */
    public PullRequest setBase(PullRequestMarker base) {
        this.base = base;
        return this;
    }

    /**
     * @return head
     */
    public PullRequestMarker getHead() {
        return head;
    }

    /**
     * @param head Head
     * @return this pull request
     */
    public PullRequest setHead(PullRequestMarker head) {
        this.head = head;
        return this;
    }

    /**
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body Body
     * @return this pull request
     */
    public PullRequest setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * @return bodyHtml
     */
    public String getBodyHtml() {
        return bodyHtml;
    }

    /**
     * @param bodyHtml Body html
     * @return this pull request
     */
    public PullRequest setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
        return this;
    }

    /**
     * @return bodyText
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     * @param bodyText Body Text
     * @return this pull request
     */
    public PullRequest setBodyText(String bodyText) {
        this.bodyText = bodyText;
        return this;
    }

    /**
     * @return diffUrl
     */
    public String getDiffUrl() {
        return diffUrl;
    }

    /**
     * @param diffUrl Diff URL
     * @return this pull request
     */
    public PullRequest setDiffUrl(String diffUrl) {
        this.diffUrl = diffUrl;
        return this;
    }

    /**
     * @return htmlUrl
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * @param htmlUrl HTML Url
     * @return this pull request
     */
    public PullRequest setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
        return this;
    }

    /**
     * @return issueUrl
     */
    public String getIssueUrl() {
        return issueUrl;
    }

    /**
     * @param issueUrl Issue URL
     * @return this pull request
     */
    public PullRequest setIssueUrl(String issueUrl) {
        this.issueUrl = issueUrl;
        return this;
    }

    /**
     * @return patchUrl
     */
    public String getPatchUrl() {
        return patchUrl;
    }

    /**
     * @param patchUrl Patch URL
     * @return this pull request
     */
    public PullRequest setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
        return this;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state State
     * @return this pull request
     */
    public PullRequest setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title Title
     * @return this pull request
     */
    public PullRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url URL
     * @return this pull request
     */
    public PullRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return mergedBy
     */
    public User getMergedBy() {
        return mergedBy;
    }

    /**
     * @param mergedBy Merged By
     * @return this pull request
     */
    public PullRequest setMergedBy(User mergedBy) {
        this.mergedBy = mergedBy;
        return this;
    }

    /**
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user User
     * @return this pull request
     */
    public PullRequest setUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id ID
     * @return this pull request
     */
    public PullRequest setId(long id) {
        this.id = id;
        return this;
    }

    /**
     * @return milestone
     */
    public Milestone getMilestone() {
        return milestone;
    }

    /**
     * @param milestone Milestone
     * @return this pull request
     */
    public PullRequest setMilestone(Milestone milestone) {
        this.milestone = milestone;
        return this;
    }

    /**
     * @return assignee
     */
    public User getAssignee() {
        return assignee;
    }

    /**
     * @param assignee Assignee
     * @return this pull request
     */
    public PullRequest setAssignee(User assignee) {
        this.assignee = assignee;
        return this;
    }

    @Override
    public String toString() {
        return "Pull Request " + number; //$NON-NLS-1$
    }
}

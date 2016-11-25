package com.github.avarabyeu.jashing.integration.github.model;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Andrei Varabyeu
 */
public class Issue {
    private Repository repository;
    @SerializedName("created_at")
    private LocalDateTime createdAt;
    private List<Label> labels;
    private List<User> assignees;
    private PullRequestInfo pullRequest;

    public PullRequestInfo getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(PullRequestInfo pullRequest) {
        this.pullRequest = pullRequest;
    }

    public List<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<User> assignees) {
        this.assignees = assignees;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("repository", repository)
                .add("createdAt", createdAt)
                .add("labels", labels)
                .add("assignees", assignees)
                .toString();
    }
}

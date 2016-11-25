package com.github.avarabyeu.jashing.integration.stackoverflow.model;

import com.google.common.base.MoreObjects;

import java.time.Instant;
import java.util.List;

/**
 * @author Andrei Varabyeu
 */
public class Question {

    private List<String> tags;
    private Owner owner;
    private boolean isAnswered;
    private long viewCount;
    private long answerCount;
    private long score;
    private Instant lastActivityDate;
    private Instant creationDate;
    private Instant questionId;
    private String link;
    private String title;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(long answerCount) {
        this.answerCount = answerCount;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Instant getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Instant lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Instant questionId) {
        this.questionId = questionId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("tags", tags)
                .add("owner", owner)
                .add("isAnswered", isAnswered)
                .add("viewCount", viewCount)
                .add("answerCount", answerCount)
                .add("score", score)
                .add("lastActivityDate", lastActivityDate)
                .add("creationDate", creationDate)
                .add("questionId", questionId)
                .add("link", link)
                .add("title", title)
                .toString();
    }
}

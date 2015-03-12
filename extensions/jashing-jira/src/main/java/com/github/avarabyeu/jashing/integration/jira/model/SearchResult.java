package com.github.avarabyeu.jashing.integration.jira.model;

import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Objects;

/**
 * Represents search results - links to issues matching given filter (JQL query) with basic
 * information supporting the paging through the results.
 *
 * @since v0.2
 */
public class SearchResult {
    private int startIndex;
    private int maxResults;
    private int total;
    private List<BasicIssue> issues;


    /**
     * @return 0-based start index of the returned issues (e.g. "3" means that 4th, 5th...maxResults issues matching given query
     * have been returned.
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * @return maximum page size (the window to results).
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * @return total number of issues (regardless of current maxResults and startIndex) matching given criteria.
     * Query JIRA another time with different startIndex to get subsequent issues
     */
    public int getTotal() {
        return total;
    }

    public List<BasicIssue> getIssues() {
        return issues;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).
                add("startIndex", startIndex).
                add("maxResults", maxResults).
                add("total", total).
                add("issues", issues).
                toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SearchResult) {
            SearchResult that = (SearchResult) obj;
            return Objects.equals(this.startIndex, that.startIndex)
                    && Objects.equals(this.maxResults, that.maxResults)
                    && Objects.equals(this.total, that.total)
                    && Objects.equals(this.issues, that.issues);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIndex, maxResults, total, issues);
    }
}

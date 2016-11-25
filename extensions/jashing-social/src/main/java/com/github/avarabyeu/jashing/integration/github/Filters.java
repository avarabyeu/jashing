package com.github.avarabyeu.jashing.integration.github;

import com.github.avarabyeu.jashing.integration.github.model.Issue;

import java.util.function.Predicate;

/**
 * @author Andrei Varabyeu
 */
public final class Filters {

    /**
     * Statics only
     */
    private Filters() {
    }

    public static Predicate<Issue> IS_PULL_REQUEST = issue -> null != issue.getPullRequest();

    public static Predicate<Issue> IS_ISSUE = IS_PULL_REQUEST.negate();

}

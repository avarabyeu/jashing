package com.github.avarabyeu.jashing.integration.jira.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.net.URI;

/**
 * @author Andrei Varabyeo
 */
public class BasicIssue {

    private URI self;

    private String key;

    /**
     * @return URI of this issue
     */
    public URI getSelf() {
        return self;
    }

    /**
     * @return issue key
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).
                add("self", self).
                add("key", key).
                toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasicIssue) {
            BasicIssue that = (BasicIssue) obj;
            return Objects.equal(this.self, that.self)
                    && Objects.equal(this.key, that.key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(self, key);
    }
}

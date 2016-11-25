package com.github.avarabyeu.jashing.integration.stackoverflow.model;

import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * @author Andrei Varabyeu
 */
public class StackExchangeResponse<T> {
    private List<T> items;
    private boolean hasMore;
    private long quotaMax;
    private long quotaRemaining;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public long getQuotaMax() {
        return quotaMax;
    }

    public void setQuotaMax(long quotaMax) {
        this.quotaMax = quotaMax;
    }

    public long getQuotaRemaining() {
        return quotaRemaining;
    }

    public void setQuotaRemaining(long quotaRemaining) {
        this.quotaRemaining = quotaRemaining;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("items", items)
                .add("hasMore", hasMore)
                .add("quotaMax", quotaMax)
                .add("quotaRemaining", quotaRemaining)
                .toString();
    }
}

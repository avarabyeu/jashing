package com.github.avarabyeu.jashing.integration.vcs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Map;

/**
 * Basic operations with VCS systems
 *
 * @author Andrey Vorobyov
 */
public interface VCSClient {

    /**
     * Returns commits count per user for specified time period
     *
     * @param from start time
     * @param to   end time
     * @return {@code {Map<User name, Commits count></User>}}
     */
    Map<String, Integer> getCommitsPerUser(@Nonnull Instant from, @Nullable Instant to);

    /**
     * Returns commits count per user from specified time till now
     *
     * @param from start time
     * @return {@code {Map<User name, Commits count></User>}}
     */
    Map<String, Integer> getCommitsPerUser(@Nonnull Instant from);

    /**
     * Returns commits count for all users for specified time period
     *
     * @param from start time
     * @param to   end time
     * @return commits count
     */
    long getCommitsForPeriod(@Nonnull Instant from, @Nullable Instant to);

    /**
     * Returns commits count for all users from specified specified time till now
     *
     * @param from start time
     * @return commits count
     */
    long getCommitsForPeriod(@Nonnull Instant from);
}

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

    Map<String, Integer> getCommitsPerUser(@Nonnull Instant from, @Nullable Instant to);

    Map<String, Integer> getCommitsPerUser(@Nonnull Instant from);

    long getCommitsForPeriod(@Nonnull Instant from, @Nullable Instant to);

    long getCommitsForPeriod(@Nonnull Instant from);
}

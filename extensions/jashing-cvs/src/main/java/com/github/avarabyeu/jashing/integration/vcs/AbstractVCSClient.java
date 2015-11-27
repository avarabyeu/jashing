package com.github.avarabyeu.jashing.integration.vcs;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Map;

/**
 * Some basic methods for {@link com.github.avarabyeu.jashing.integration.vcs.VCSClient}
 * Just delegate calls to appropriate methods
 *
 * @author Andrey Vorobyov
 */
public abstract class AbstractVCSClient implements VCSClient {

    @Override
    public Map<String, Long> getCommitsPerUser(@Nonnull Instant from) {
        return getCommitsPerUser(from, null);
    }

    @Override
    public long getCommitsForPeriod(@Nonnull Instant from) {
        return getCommitsForPeriod(from, null);
    }
}

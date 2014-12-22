package com.github.avarabyeu.jashing.integration.vcs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Composite VCS client implementation. Merges all results into one
 *
 * @author Andrey Vorobyov
 */
public class CompositeVCSClient extends AbstractVCSClient implements VCSClient {

    private final Collection<VCSClient> delegates;

    public CompositeVCSClient(@Nonnull Collection<VCSClient> delegates) {
        this.delegates = delegates;
    }

    @Override
    public Map<String, Integer> getCommitsPerUser(@Nonnull Instant from, @Nullable Instant to) {
        List<Map<String, Integer>> results = new ArrayList<>(delegates.size());
        results.addAll(delegates.stream().map(delegate -> delegate.getCommitsPerUser(from, to)).collect(Collectors.toList()));
        return results.stream()
                .map(Map::entrySet)          // converts each map into an entry set
                .flatMap(Collection::stream) // converts each set into an entry stream, then
                        // "concatenates" it in place of the original set
                .collect(
                        Collectors.toMap(        // collects into a map
                                Map.Entry::getKey,   // where each entry is based
                                Map.Entry::getValue, // on the entries in the stream
                                Integer::sum         // such that if a value already exist for
                                // a given key, the sum of the old
                                // and new value is taken
                        )
                );
    }


    @Override
    public long getCommitsForPeriod(@Nonnull Instant from, @Nullable Instant to) {
        int count = 0;
        for (VCSClient delegate : delegates) {
            count += delegate.getCommitsForPeriod(from, to);
        }
        return count;
    }

}

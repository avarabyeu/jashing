package com.github.avarabyeu.jashing.integration.vcs;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.ListEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Produces top commiters for specified VCS in format [{"commitee" : commitsCount}]
 * <p>
 * Contains <b>required</b> parameter 'logPeriod' - period in days for VCS history search
 *
 * @author Andrey Vorobyov
 */
@EventSource(value = "vcs-top-committers-source", explicitConfiguration = AbstractVcsModule.class)
public class VCSTopCommitersEventSource extends ScheduledEventSource<ListEvent<Integer>> {

    private static final Comparator<ListEvent.Item<Integer>> ITEM_COMPARATOR = ((item1, item2) -> item1.getValue().compareTo(item2.getValue()));

    @Inject
    private VCSClient svnClient;

    @Inject
    @Named("forPeriod")
    private Double daysBefore;


    @Override
    protected ListEvent<Integer> produceEvent() {
        LocalDateTime fromDateTime = LocalDate.now().minusDays(daysBefore.longValue()).atStartOfDay();
        Map<String, Integer> commitsPerUser = svnClient.getCommitsPerUser(fromDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return new ListEvent<>(commitsPerUser.entrySet()
                .stream()
                .map(entry -> new ListEvent.Item<>(entry.getKey(), entry.getValue()))
                .sorted(ITEM_COMPARATOR.reversed())
                .collect(Collectors.toList()));
    }
}

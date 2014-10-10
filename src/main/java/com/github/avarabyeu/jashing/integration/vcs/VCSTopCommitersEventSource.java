package com.github.avarabyeu.jashing.integration.vcs;

import com.github.avarabyeu.jashing.core.eventsource.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.ListEvent;

import javax.inject.Inject;
import javax.inject.Named;
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
@HandlesEvent(Events.LIST)
public class VCSTopCommitersEventSource extends ScheduledEventSource<ListEvent<Integer>> {

    public static final Comparator<ListEvent.Item<Integer>> ITEM_COMPARATOR = ((item1, item2) -> item1.getValue().compareTo(item2.getValue()));

    @Inject
    private VCSClient svnClient;

    @Inject
    @Named("forPeriod")
    private Double daysBefore;


    @Override
    protected ListEvent<Integer> produceEvent() {
        LocalDateTime fromDateTime = LocalDateTime.now().minusDays(daysBefore.longValue());
        Map<String, Integer> commitsPerUser = svnClient.getCommitsPerUser(fromDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return new ListEvent<Integer>(commitsPerUser.entrySet()
                .stream()
                .map(entry -> new ListEvent.Item<Integer>(entry.getKey(), entry.getValue()))
                .sorted(ITEM_COMPARATOR.reversed())
                .collect(Collectors.toList()));
    }
}

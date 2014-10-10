package com.github.avarabyeu.jashing.integration.vcs;

import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.ListEvent;
import com.github.avarabyeu.jashing.eventsource.HandlesEvent;
import com.github.avarabyeu.jashing.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.eventsource.annotation.EventId;
import com.github.avarabyeu.jashing.eventsource.annotation.Frequency;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;
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


    @Inject
    public VCSTopCommitersEventSource(@EventId String eventId, @Frequency Duration period) {
        super(eventId, period);
    }

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

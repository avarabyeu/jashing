package com.github.avarabyeu.jashing.integration.vcs

import com.github.avarabyeu.jashing.core.EventSource
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource
import com.github.avarabyeu.jashing.events.ListEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Produces top commiters for specified VCS in format [{"commitee" : commitsCount}]
 * <p>
 * Contains <b>required</b> parameter 'logPeriod' - period in days for VCS history search
 *
 * @author Andrey Vorobyov
 */
@EventSource(value = "vcs-top-committers-source", explicitConfiguration = AbstractVcsModule::class)
class VCSTopCommitersEventSource : ScheduledEventSource<ListEvent<Long>>() {

    private val ITEM_COMPARATOR: Comparator<ListEvent.Item<Long>> = Comparator { item, item2 -> item.value.compareTo(item2.value) }


    @Inject
    lateinit var svnClient: VCSClient

    @Inject
    @Named("forPeriod")
    var daysBefore: Double? = null


    override fun produceEvent(): ListEvent<Long> {
        val fromDateTime: LocalDateTime = LocalDate.now().minusDays(daysBefore!!.toLong()).atStartOfDay()
        val commitsPerUser = svnClient
                .getCommitsPerUser(fromDateTime.atZone(ZoneId.systemDefault()).toInstant())
        return ListEvent(commitsPerUser.map { ListEvent.Item(it.key, it.value) }.sortedWith(ITEM_COMPARATOR.reversed()))
    }
}

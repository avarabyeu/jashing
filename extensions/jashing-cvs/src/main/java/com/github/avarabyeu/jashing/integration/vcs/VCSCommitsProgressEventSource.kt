package com.github.avarabyeu.jashing.integration.vcs

import com.github.avarabyeu.jashing.core.EventSource
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource
import com.github.avarabyeu.jashing.events.NumberEvent
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Andrey Vorobyov
 */
@EventSource(value = "vcs-commits-progress-source", explicitConfiguration = AbstractVcsModule::class)
class VCSCommitsProgressEventSource : ScheduledEventSource<NumberEvent>() {

    @Inject
    lateinit var vcsClient: VCSClient

    /* recalculate yesterday commits count each hour. Think about better approach of expiration policy*/
    val yesterdayCommitsCount: Supplier<Long> = Suppliers.memoizeWithExpiration({
        val today = LocalDate.now()
        vcsClient.getCommitsForPeriod(today.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                today.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }, 1, TimeUnit.HOURS)


    override fun produceEvent(): NumberEvent {
        val fromDateTime: Instant = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
        val todaysCommitsCount: Long = vcsClient.getCommitsForPeriod(fromDateTime)
        return NumberEvent(todaysCommitsCount.toInt(), yesterdayCommitsCount.get().toInt())
    }
}

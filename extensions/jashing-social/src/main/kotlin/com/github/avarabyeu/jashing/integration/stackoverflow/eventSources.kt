package com.github.avarabyeu.jashing.integration.stackoverflow

import com.github.avarabyeu.jashing.core.EventSource
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource
import com.github.avarabyeu.jashing.events.StatusAwareNumberEvent
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named

@EventSource(value = "stackoverflow-not-answered-source", explicitConfiguration = StackOverflowModule::class)
class StackOverflowUnansweredSource @Inject constructor(client: StackExchangeClient, @Named("stackoverflow.question.tag") tag: String) :
        ScheduledEventSource<StatusAwareNumberEvent>() {

    val client: StackExchangeClient
    val tag: String

    init {
        this.client = client
        this.tag = tag
    }

    override fun produceEvent(): StatusAwareNumberEvent {
        val questions = client
                .getTaggedQuestions(tag)
                .items
                //NOT closed
                .filter { null == it.closedDate }
                //NOT answered
                .filter({ !it.isAnswered })

        val count = questions.size
        val notTodayCount = questions
                .count {
                    it.creationDate
                            .isBefore(LocalDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
                }
        return StatusAwareNumberEvent(
                if (count > 0) StatusAwareNumberEvent.Status.warning else StatusAwareNumberEvent.Status.ok, count,
                notTodayCount)
    }
}

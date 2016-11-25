package com.github.avarabyeu.jashing.integration.stackoverflow;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.StatusAwareNumberEvent;
import com.github.avarabyeu.jashing.integration.stackoverflow.model.Question;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = "stackoverflow-not-answered-source", explicitConfiguration = StackOverflowModule.class)
public class StackOverflowUnansweredSource extends ScheduledEventSource<StatusAwareNumberEvent> {

    @Inject
    private StackExchangeClient client;

    @Inject
    @Named("stackoverflow.question.tag")
    private String tag;

    @Override
    protected StatusAwareNumberEvent produceEvent() {
        final List<Question> questions = client.getTaggedQuestions(tag).getItems();

        final int count = questions.size();
        final Long notTodayCount = questions.stream()
                .filter(q -> q.getCreationDate()
                        .isBefore(LocalDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .count();
        return new StatusAwareNumberEvent(
                count > 0 ? StatusAwareNumberEvent.Status.warning : StatusAwareNumberEvent.Status.ok, count,
                notTodayCount.intValue());
    }
}

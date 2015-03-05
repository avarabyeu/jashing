package com.github.avarabyeu.jashing.integration.jenkins;

import com.github.avarabyeu.jashing.core.eventsource.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.MeterEvent;
import com.github.avarabyeu.restendpoint.http.exception.RestEndpointClientException;
import com.google.common.util.concurrent.AbstractScheduledService;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * @author Andrei Varabyeu
 */
@HandlesEvent(Events.METER)
public class JenkinsActiveJobsEventSource extends ScheduledEventSource<MeterEvent> {

    @Inject
    private JenkinsClient jenkinsClient;

    @Override
    protected MeterEvent produceEvent() {
        Jobs runningJobs = jenkinsClient.getRunningJobs();
        if (!runningJobs.getNames().isEmpty()) {
            new JobTracker(runningJobs.getNames().get(0)).startAsync().awaitTerminated();
            return null;
        }

        /* no any updates */
        return null;
    }


    public class JobTracker extends AbstractScheduledService {

        private final String job;

        public JobTracker(String job) {
            this.job = job;
        }

        @Override
        protected void runOneIteration() throws Exception {
            try {
                sendEvent(new MeterEvent(job, Byte.valueOf(jenkinsClient.getJobProgress(job))));
            } catch (RestEndpointClientException e) {
                shutDown();
            }


        }

        @Override
        protected Scheduler scheduler() {
            return AbstractScheduledService.Scheduler.newFixedDelaySchedule(1, 1, TimeUnit.SECONDS);
        }
    }


}

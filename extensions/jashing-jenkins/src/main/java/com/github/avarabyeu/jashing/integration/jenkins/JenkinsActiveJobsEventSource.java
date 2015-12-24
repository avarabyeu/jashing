package com.github.avarabyeu.jashing.integration.jenkins;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.MeterEvent;
import com.google.common.util.concurrent.Uninterruptibles;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = "jenkins-active-jobs-source", explicitConfiguration = JenkinsModule.class)
public class JenkinsActiveJobsEventSource extends ScheduledEventSource<MeterEvent> {

    @Inject
    private JenkinsClient jenkinsClient;

    @Override
    protected MeterEvent produceEvent() {
        Jobs runningJobs = jenkinsClient.getRunningJobs();
        if (!runningJobs.getNames().isEmpty()) {
            new JobTracker(runningJobs.getNames().get(0)).track();
            return null;
        }

        /* no any updates */
        return null;
    }

    public class JobTracker {

        private final String job;

        public JobTracker(String job) {
            this.job = job;
        }

        protected void track() {
            Byte progress = 100;
            do {
                try {
                    progress = jenkinsClient.getJobProgress(job).exceptionally(t -> (byte) 100).get();
                    sendEvent(new MeterEvent(job, progress));
                    Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } while (progress < 100);

        }
    }

}

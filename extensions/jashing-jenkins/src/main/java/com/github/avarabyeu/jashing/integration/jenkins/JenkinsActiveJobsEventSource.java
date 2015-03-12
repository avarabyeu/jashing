package com.github.avarabyeu.jashing.integration.jenkins;

import com.github.avarabyeu.jashing.core.HandlesEvent;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.Events;
import com.github.avarabyeu.jashing.events.MeterEvent;
import com.github.avarabyeu.wills.Wills;
import com.google.common.util.concurrent.Uninterruptibles;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * @author Andrei Varabyeu
 */
@HandlesEvent(value = Events.METER, explicitConfiguration = JenkinsModule.class)
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
            Byte progress;
            do {
                progress = jenkinsClient.getJobProgress(job).replaceFailed(Wills.of((byte) 100)).obtain();
                sendEvent(new MeterEvent(job, progress));
                Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
            } while (progress < 100);

        }
    }


}

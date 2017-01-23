package scheduler;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerTask {

    private Scheduler mScheduler;

    public SchedulerTask() throws SchedulerException {
        mScheduler = new StdSchedulerFactory().getScheduler();

        JobDetail job = newJob(JobChecker.class)
                .withIdentity("j1", "g1").build();

        Trigger trigger = newTrigger()
                .withIdentity("t1", "g1").startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever()).build();

        mScheduler.scheduleJob(job, trigger);
    }

    public void start() throws SchedulerException {
        mScheduler.start();
    }

    public void stop() throws SchedulerException {
        mScheduler.shutdown(true);
    }
}

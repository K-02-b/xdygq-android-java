package com.example.xdygq3;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MyJobService extends JobService {
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }
    @Override
    public boolean onStartJob(JobParameters params) {
        EventBus.getDefault().post(new EventMessage(1,"UPDATE"));
        scheduleJobAgain();
        return false;
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEvent(EventMessage event) {}
    private void scheduleJobAgain() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(337845818, componentName)
                .setMinimumLatency(shareData.config != null ? shareData.config.DelayTime : 10000)
                .setBackoffCriteria(10000, JobInfo.BACKOFF_POLICY_LINEAR)
                .build();
        scheduler.cancelAll();
        scheduler.schedule(jobInfo);
    }
}

package de.bolz.ampelding.jenkins;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.bolz.ampelding.jenkins.api.BuildDetails;
import de.bolz.ampelding.jenkins.api.JenkinsApiProvider;
import de.bolz.ampelding.jenkins.api.JenkinsApi;
import de.bolz.ampelding.jenkins.api.JobStatus;

/**
 * Created by Johannes Bolz on 29.12.16.
 */

public class RequestRunner {

    private ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(2);
    private Map<JobInfo, ScheduledFuture> monitoredJobs = new HashMap<>();
    private Map<String, JenkinsApi> apiMap = new HashMap<>();
    private long scheduleInterval;
    private JenkinsApiProvider jenkinsApiProvider;
    private Handler handler;

    public RequestRunner(JenkinsApiProvider apiProvider, long scheduleInterval) {
        this.scheduleInterval = scheduleInterval;
        this.jenkinsApiProvider = apiProvider;
        handler = new Handler();
    }

    public synchronized void monitorJob(JobInfo jobInfo, JobQueryListener listener) {
        ScheduledFuture inMap = monitoredJobs.remove(jobInfo);
        if (inMap != null) {
            inMap.cancel(false);
        }
        JobQuery jobQuery = new JobQuery(jobInfo, jenkinsApiProvider.buildApiForBaseUrl(jobInfo.baseUrl),
                listener);
        ScheduledFuture newOne = scheduledExecutor.scheduleAtFixedRate(jobQuery, 0, scheduleInterval,
                TimeUnit.MILLISECONDS);
        monitoredJobs.put(jobInfo, newOne);
    }

    public synchronized void unmonitorJob(JobInfo jobInfo) {
        monitoredJobs.remove(jobInfo).cancel(false);
    }

    public synchronized void stop() {
        scheduledExecutor.shutdown();
        monitoredJobs.clear();
    }

    public interface JobQueryListener {
        void onQueryResult(JobInfo jobInfo, BuildDetails buildDetails);
        void onError(JobInfo jobInfo, Throwable t);
    }

    private class JobQuery implements Runnable {

        private final JobInfo jobInfo;
        private JenkinsApi jenkinsApi;
        private JobQueryListener jobQueryListener;

        JobQuery(JobInfo jobInfo, JenkinsApi jenkinsApi, JobQueryListener listener) {
            this.jobInfo = jobInfo;
            this.jenkinsApi = jenkinsApi;
            this.jobQueryListener = listener;
        }

        @Override
        public void run() {
            try {
                BuildDetails buildDetails = null;
                JobStatus status = jenkinsApi.getJobStatus(jobInfo.jenkinsUser, jobInfo.apiToken, jobInfo.jobName);
                if (status != null && status.lastBuild != null) {
                    buildDetails = jenkinsApi.getBuildDetails(jobInfo.jenkinsUser, jobInfo.apiToken, jobInfo.jobName,
                            status.lastBuild.number);
                }
                final BuildDetails toPost = buildDetails;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (toPost != null) {
                            jobQueryListener.onQueryResult(jobInfo, toPost);
                        } else {
                            jobQueryListener.onError(jobInfo, new IllegalStateException("couldn't get job info"));
                        }
                    }
                });
            } catch (final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        jobQueryListener.onError(jobInfo, e);
                    }
                });
            }
        }
    }

    public static class JobInfo {

        private final String baseUrl, jobName, jenkinsUser, apiToken;

        public JobInfo(@NonNull String baseUrl, @NonNull String jobName, @NonNull String jenkinsUser,
                       @NonNull String apiToken) {
            this.baseUrl = baseUrl;
            this.jobName = jobName;
            this.jenkinsUser = jenkinsUser;
            this.apiToken = apiToken;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof JobInfo)) {
                return false;
            }
            JobInfo toCompare = (JobInfo) obj;
            return baseUrl.equals(toCompare.baseUrl)
                    && jobName.equals(toCompare.jobName);
        }
    }
}
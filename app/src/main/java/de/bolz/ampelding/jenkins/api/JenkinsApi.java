package de.bolz.ampelding.jenkins.api;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Created by Johannes Bolz on 28.12.16.
 */
public interface JenkinsApi {
    JobStatus getJobStatus(@NonNull String user, @NonNull String token, @NonNull String jobName) throws IOException;
    BuildDetails getBuildDetails(@NonNull String user, @NonNull String token, @NonNull String jobName, int buildNumber) throws IOException;
}

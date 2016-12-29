package de.bolz.ampelding.jenkins.api.retrofit;

import de.bolz.ampelding.jenkins.api.BuildDetails;
import de.bolz.ampelding.jenkins.api.JobStatus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by Johannes Bolz on 28.12.16.
 */

public interface RetrofitApiCalls {

    @GET("job/{jobName}/api/json")
    Call<JobStatus> getJobStatus(@Header("Authorization") String authHeaderValue, @Path("jobName") String jobName);

    @GET("job/{jobName}/{buildNumber}/api/json")
    Call<BuildDetails> getBuildDetails(@Header("Authorization") String authHeaderValue,
                                       @Path("jobName") String jobName, @Path("buildNumber") String buildNumber);
}

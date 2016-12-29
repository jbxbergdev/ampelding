package de.bolz.ampelding.jenkins.api.retrofit;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.IOException;

import de.bolz.ampelding.jenkins.api.JenkinsApi;
import de.bolz.ampelding.jenkins.api.BuildDetails;
import de.bolz.ampelding.jenkins.api.JobStatus;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Johannes Bolz on 28.12.16.
 */

public class RetrofitJenkinsApi implements JenkinsApi {

    private RetrofitApiCalls retrofitApiCalls;

    public RetrofitJenkinsApi(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        retrofitApiCalls = retrofit.create(RetrofitApiCalls.class);
    }

    @Override
    public JobStatus getJobStatus(@NonNull String user, @NonNull String token, @NonNull String jobName)
            throws IOException {
        return retrofitApiCalls.getJobStatus(buildAuthString(user, token), jobName).execute().body();
    }

    @Override
    public BuildDetails getBuildDetails(@NonNull String user, @NonNull String token, @NonNull String jobName,
                                        int buildNumber) throws IOException {
        return retrofitApiCalls.getBuildDetails(buildAuthString(user, token), jobName, String.valueOf(buildNumber))
                .execute().body();
    }

    private String buildAuthString(String user, String password) {
        return "Basic " + Base64.encodeToString((user + ":" + password).getBytes(), Base64.NO_WRAP);
    }
}

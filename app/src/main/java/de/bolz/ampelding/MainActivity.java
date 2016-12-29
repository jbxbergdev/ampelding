package de.bolz.ampelding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;

import de.bolz.ampelding.device.DeviceManager;
import de.bolz.ampelding.jenkins.RequestRunner;
import de.bolz.ampelding.jenkins.api.BuildDetails;

public class MainActivity extends Activity implements RequestRunner.JobQueryListener {

    private static final String EXTRA_INDICATOR_ID = "indicator_id";
    private static final String EXTRA_BASE_URL = "base_url";
    private static final String EXTRA_JOB_NAME = "job_name";
    private static final String EXTRA_JENKINS_USER = "jenkins_user";
    private static final String EXTRA_API_TOKEN = "api_token";

    private BuildIndicator buildIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String indicatorId = intent.getStringExtra(EXTRA_INDICATOR_ID);
        String baseUrl = intent.getStringExtra(EXTRA_BASE_URL);
        String jobName = intent.getStringExtra(EXTRA_JOB_NAME);
        String jenkinsUser = intent.getStringExtra(EXTRA_JENKINS_USER);
        String apiToken = intent.getStringExtra(EXTRA_API_TOKEN);
        if (indicatorId != null && baseUrl != null && jobName != null && jenkinsUser != null && apiToken != null) {
            buildIndicator = DeviceManager.getDevice().getBuildIndicators()[Integer.parseInt(indicatorId)];
            try {
                buildIndicator.open();
                buildIndicator.changeState(BuildIndicator.STATE_UNKNOWN);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            ((App) getApplication()).getRequestRunner().monitorJob(
                    new RequestRunner.JobInfo(baseUrl, jobName, jenkinsUser, apiToken), this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((App) getApplication()).getRequestRunner().stop();
        try {
            if (buildIndicator != null) {
                buildIndicator.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onQueryResult(RequestRunner.JobInfo jobInfo, BuildDetails buildDetails) {
        try {
            if (buildDetails.building) {
                buildIndicator.changeState(BuildIndicator.STATE_INDETERMINATE);
            } else {
                switch (buildDetails.result) {
                    case BuildDetails.SUCCESS:
                        buildIndicator.changeState(BuildIndicator.STATE_POSITIVE);
                        break;
                    default:
                        buildIndicator.changeState(BuildIndicator.STATE_NEGATIVE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(RequestRunner.JobInfo jobInfo, Throwable t) {
        try {
            buildIndicator.changeState(BuildIndicator.STATE_UNKNOWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

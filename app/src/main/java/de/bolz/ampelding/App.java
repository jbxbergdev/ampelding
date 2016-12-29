package de.bolz.ampelding;

import android.app.Application;

import de.bolz.ampelding.jenkins.RequestRunner;
import de.bolz.ampelding.jenkins.api.JenkinsApiProvider;

/**
 * Created by Johannes Bolz on 29.12.16.
 */

public class App extends Application {

    private static final long REQUEST_FREQUENCY_MS = 10000;

    private RequestRunner requestRunner;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public synchronized RequestRunner getRequestRunner() {
        if (requestRunner == null) {
            requestRunner = new RequestRunner(new JenkinsApiProvider(), REQUEST_FREQUENCY_MS);
        }
        return requestRunner;
    }
}

package de.bolz.ampelding.jenkins.api;

import java.util.HashMap;
import java.util.Map;

import de.bolz.ampelding.jenkins.api.retrofit.RetrofitJenkinsApi;

/**
 * Created by Johannes Bolz on 29.12.16.
 */

public class JenkinsApiProvider {

    private Map<String, JenkinsApi> apiMap = new HashMap<>();

    public synchronized JenkinsApi buildApiForBaseUrl(String baseUri) {
        JenkinsApi api = apiMap.get(baseUri);
        if (api == null) {
            api = new RetrofitJenkinsApi(baseUri);
            apiMap.put(baseUri, api);
        }
        return api;
    }

}

package de.bolz.ampelding.jenkins.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Johannes Bolz on 28.12.16.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildDetails {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    @JsonProperty("building")
    public boolean building;

    @JsonProperty("result")
    public String result;

    @JsonProperty("number")
    public int number;

    @JsonProperty("url")
    public String url;
}

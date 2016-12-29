package de.bolz.ampelding.jenkins.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Johannes Bolz on 28.12.16.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildInfo {

    @JsonProperty("number")
    public int number;

    @JsonProperty("url")
    public String url;
}

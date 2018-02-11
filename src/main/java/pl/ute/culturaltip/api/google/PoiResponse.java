package pl.ute.culturaltip.api.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by dominik on 05.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoiResponse {

    @JsonProperty("error_message")
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private List<PoiResponseResult> results;

    public List<PoiResponseResult> getResults() {
        return results;
    }

    public void setResults(List<PoiResponseResult> results) {
        this.results = results;
    }
}


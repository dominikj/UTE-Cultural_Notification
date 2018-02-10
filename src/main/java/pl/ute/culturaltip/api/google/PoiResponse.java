package pl.ute.culturaltip.api.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by dominik on 05.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoiResponse {
    private List<PoiResponseResult> results;

    public List<PoiResponseResult> getResults() {
        return results;
    }

    public void setResults(List<PoiResponseResult> results) {
        this.results = results;
    }
}


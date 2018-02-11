package pl.ute.culturaltip.api.wikipedia.opensearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by dominik on 11.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenSearchResponse {

    private OpenSearchQuery query;

    public OpenSearchQuery getQuery() {
        return query;
    }

    public void setQuery(OpenSearchQuery query) {
        this.query = query;
    }
}

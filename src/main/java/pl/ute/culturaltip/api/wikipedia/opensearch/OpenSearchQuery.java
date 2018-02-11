package pl.ute.culturaltip.api.wikipedia.opensearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by dominik on 11.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenSearchQuery {
    private List<OpenSearchItem> search;

    public List<OpenSearchItem> getSearch() {
        return search;
    }

    public void setSearch(List<OpenSearchItem> search) {
        this.search = search;
    }
}

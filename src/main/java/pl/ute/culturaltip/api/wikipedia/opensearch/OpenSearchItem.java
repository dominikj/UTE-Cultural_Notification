package pl.ute.culturaltip.api.wikipedia.opensearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by dominik on 11.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenSearchItem {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

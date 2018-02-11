package pl.ute.culturaltip.api.wikipedia.extracts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by dominik on 11.02.18.
 */
public class ExtractsArticleResponse {

    private ExtractsArticleQuery query;
    private String batchcomplete;

    public String getBatchcomplete() {
        return batchcomplete;
    }

    public void setBatchcomplete(String batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public ExtractsArticleQuery getQuery() {
        return query;
    }

    public void setQuery(ExtractsArticleQuery query) {
        this.query = query;
    }
}

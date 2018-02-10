package pl.ute.culturaltip.restapiutils;

import java.util.Map;

/**
 * Created by dominik on 05.02.18.
 */
public class RestApiParams {

    private String uri;
    private Map<String, String> queryParams;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }
}

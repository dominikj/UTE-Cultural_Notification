package pl.ute.culturaltip.api.wikipedia.extracts;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * Created by dominik on 11.02.18.
 */

public class ExtractsArticleQuery {
    private Object pages;

    @JsonRawValue
    public Object getPages() {
        return pages;

    }

    public void setPages(Object pages) {
        this.pages = pages;
    }
}

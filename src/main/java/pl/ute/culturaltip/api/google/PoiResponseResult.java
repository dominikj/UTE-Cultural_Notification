package pl.ute.culturaltip.api.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by dominik on 10.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoiResponseResult {

    private PoiGeometry geometry;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PoiGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(PoiGeometry geometry) {
        this.geometry = geometry;
    }
}

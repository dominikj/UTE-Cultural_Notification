package pl.ute.culturaltip.api.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by dominik on 10.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoiGeometry {
    private PoiLocation location;

    public PoiLocation getLocation() {
        return location;
    }

    public void setLocation(PoiLocation location) {
        this.location = location;
    }
}

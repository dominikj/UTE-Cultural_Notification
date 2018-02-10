package pl.ute.culturaltip.api.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by dominik on 10.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoiLocation {
    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}

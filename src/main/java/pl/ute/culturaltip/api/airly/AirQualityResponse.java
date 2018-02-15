package pl.ute.culturaltip.api.airly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQualityResponse {
    private int pollutionLevel;

    public int getPollutionLevel() {
        return pollutionLevel;
    }

    public void setPollutionLevel(int pollutionLevel) {
        this.pollutionLevel = pollutionLevel;
    }
}

package pl.ute.culturaltip.api.orange.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dominik on 05.02.18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendSmsResponse {
    private String result;
    private String deliveryStatus;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}

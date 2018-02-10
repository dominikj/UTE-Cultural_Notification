package pl.ute.culturaltip.data;

import java.util.Date;

import pl.ute.culturaltip.enums.NotificationStatus;

/**
 * Created by dominik on 10.02.18.
 */

public class NotificationData {
    private Date date;
    private String message;
    private NotificationStatus status;

    public NotificationData() {
        this.status = NotificationStatus.WAITING_TO_SEND;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
}

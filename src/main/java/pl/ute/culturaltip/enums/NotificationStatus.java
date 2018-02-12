package pl.ute.culturaltip.enums;

public enum NotificationStatus {
    WAITING_TO_SEND("Waiting to send"),
    SENT_OK("Sent (OK)"),
    SENT_ERROR("Sent (Error)");

    private String name;

    NotificationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
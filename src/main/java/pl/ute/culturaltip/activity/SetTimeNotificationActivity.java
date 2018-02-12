package pl.ute.culturaltip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.data.NotificationData;

import static pl.ute.culturaltip.constants.Constants.Message.CREATED_MESSAGE;
import static pl.ute.culturaltip.constants.Constants.Notification.CREATED_NOTIFICATION;

/**
 * Created by dominik on 11.02.18.
 */

public class SetTimeNotificationActivity extends AbstractNavigationActivity
        implements TimePicker.OnTimeChangedListener {

    private TimePicker timePicker;
    private int hourOfDay;
    private int minute;

    public SetTimeNotificationActivity() {
        super(R.id.create_notification_btn, R.id.back_set_time_notification_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_notification);
    }

    @Override
    protected void onResume() {
        super.onResume(false, true);
        timePicker = (TimePicker) findViewById(R.id.time_picker_set_time_notification);
        if (timePicker != null) {
            timePicker.setOnTimeChangedListener(this);
        }
    }


    @Override
    protected Intent createIntentForForward() {
        Intent intent = new Intent(getContext(), NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Gson gson = new Gson();
        intent.putExtra(CREATED_NOTIFICATION, gson.toJson(createNotification()));
        return intent;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        boolean isBefore = getCurrentTime().before(getTime(hourOfDay, minute));
        this.hourOfDay = hourOfDay;
        this.minute = minute;

        if (isBefore) {
            enableForwardButton();
            return;
        }
        disableForwardButton();
    }

    private Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }

    private Date getTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private NotificationData createNotification() {
        NotificationData notificationData = new NotificationData();
        notificationData.setMessage(getIntent().getExtras().getString(CREATED_MESSAGE));
        notificationData.setDate(getTime(hourOfDay, minute));
        return notificationData;
    }
}

package pl.ute.culturaltip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import pl.ute.culturaltip.R;

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
        timePicker.setOnTimeChangedListener(this);
    }


    @Override
    protected Intent createIntentForForward() {
        return null;
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
}

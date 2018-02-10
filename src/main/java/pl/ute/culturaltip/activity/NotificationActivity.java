package pl.ute.culturaltip.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.example.dominik.ute.R;
import com.google.gson.Gson;

import pl.ute.culturaltip.data.NotificationData;

/**
 * Created by dominik on 10.02.18.
 */

public class NotificationActivity extends AbstractActivity {


    private NotificationData currentNotification;
    private static final String NOTIFICATION_PREFERENCIES_KEY = "notification";

    public NotificationActivity() {
        super(R.id.close_notification_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notification);
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String notification = settings.getString(NOTIFICATION_PREFERENCIES_KEY, "");
        if (!notification.trim().isEmpty()) {
            restoreCurrentNotification(notification);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = settings.edit();
            Gson gson = new Gson();
            editor.putString(NOTIFICATION_PREFERENCIES_KEY, gson.toJson(currentNotification));
            editor.apply();
        } catch (Exception e) {
            Log.e("Preferencies exception", e.getMessage(), e);

        }
    }

    private void restoreCurrentNotification(String notificationToRestore) {
        Gson gson = new Gson();
        this.currentNotification = gson.fromJson(notificationToRestore, NotificationData.class);
        if (currentNotification != null) {
            TextView currentNotification = (TextView) findViewById(R.id.current_notification_text);
            TextView currentMessage = (TextView) findViewById(R.id.current_message_text);
            TextView currentStatus = (TextView) findViewById(R.id.current_notification_status_text);
            currentNotification.setText(this.currentNotification.getDate().toString());
            currentMessage.setText(this.currentNotification.getMessage());
            currentStatus.setText(this.currentNotification.getStatus().getName());
        }
    }
}

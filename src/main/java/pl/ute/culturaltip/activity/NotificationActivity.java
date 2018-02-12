package pl.ute.culturaltip.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.data.FriendData;
import pl.ute.culturaltip.data.NotificationData;
import pl.ute.culturaltip.enums.NotificationStatus;
import pl.ute.culturaltip.service.NotificationService;

import static pl.ute.culturaltip.constants.Constants.Friend.FRIENDS_PREFERENCIES_KEY;
import static pl.ute.culturaltip.constants.Constants.Notification.CREATED_NOTIFICATION;

/**
 * Created by dominik on 10.02.18.
 */

public class NotificationActivity extends AbstractClosableActivity {

    public static final String NOTIFICATION_PREFERENCIES_KEY = "notification";
    private NotificationData currentNotificationData;
    private TextView currentNotification;
    private TextView currentMessage;
    private TextView currentStatus;
    private List<FriendData> friendList;

    public NotificationActivity() {
        super(R.id.close_notification_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notification);
        super.onCreate(savedInstanceState);

        currentNotification = (TextView) findViewById(R.id.current_notification_text);
        currentMessage = (TextView) findViewById(R.id.current_message_text);
        currentStatus = (TextView) findViewById(R.id.current_notification_status_text);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String notification = settings.getString(NOTIFICATION_PREFERENCIES_KEY, "");
        String friendList = settings.getString(FRIENDS_PREFERENCIES_KEY, "");

        if (!notification.trim().isEmpty()) {
            restoreCurrentNotification(notification);
        }

        if (!friendList.trim().isEmpty()) {
            restoreFriendList(friendList);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Button newNotificationButton = (Button) findViewById(R.id.new_notification_btn);

        newNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectPoiActivity.class));
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getString(CREATED_NOTIFICATION) != null) {
            restoreCurrentNotification(extras.getString(CREATED_NOTIFICATION));
        }

        Intent serviceIntent = new Intent(this, NotificationService.class);
        Gson gson = new Gson();
        serviceIntent.putExtra(CREATED_NOTIFICATION, gson.toJson(currentNotificationData));
        serviceIntent.putExtra(Constants.Friend.FRIENDS_LIST, gson.toJson(friendList));
        startService(serviceIntent);
        currentNotificationData.setScheduled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = settings.edit();
            Gson gson = new Gson();
            editor.putString(NOTIFICATION_PREFERENCIES_KEY, gson.toJson(currentNotificationData));
            editor.apply();
        } catch (Exception e) {
            Log.e("Preferencies exception", e.getMessage(), e);

        }
    }

    public void setNotificationStatus(NotificationStatus status) {
        currentNotificationData.setStatus(status);
        currentStatus.setText(status.getName());
    }

    private void restoreCurrentNotification(String notificationToRestore) {
        Gson gson = new Gson();
        this.currentNotificationData = gson.fromJson(notificationToRestore, NotificationData.class);
        if (currentNotificationData != null) {
            currentNotification.setText(this.currentNotificationData.getDate().toString());
            currentMessage.setText(this.currentNotificationData.getMessage());
            currentStatus.setText(this.currentNotificationData.getStatus().getName());
        }
    }

    private void restoreFriendList(String friendsToRestore) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<FriendData>>() {
        }.getType();
        this.friendList = gson.fromJson(friendsToRestore, listType);
    }
}

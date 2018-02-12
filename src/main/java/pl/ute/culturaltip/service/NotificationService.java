package pl.ute.culturaltip.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.ute.culturaltip.api.orange.sms.SendSmsHttpRequestTask;
import pl.ute.culturaltip.data.FriendData;
import pl.ute.culturaltip.data.NotificationData;
import pl.ute.culturaltip.receiver.ReceiverNotificationService;
import pl.ute.culturaltip.restapiutils.RestApiParams;

import static pl.ute.culturaltip.constants.Constants.Friend.FRIENDS_LIST;
import static pl.ute.culturaltip.constants.Constants.IntentCode.SEND_SMS_INTENT_NOTIFICATION_ACTIVITY;
import static pl.ute.culturaltip.constants.Constants.Notification.CREATED_NOTIFICATION;

/**
 * Created by dominik on 11.02.18.
 */

public class NotificationService extends IntentService {
    private static final String API_KEY = "rIeprPWnQZRXtGO9uvzAZolOPIUGZ77L";
    private static final String SEND_SMS_API_URI =
            "https://apitest.orange.pl/Messaging/v1/SMSOnnet";
    private static final String POLISH_AREA_CODE = "48";
    private ReceiverNotificationService receiverActivity;


    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        receiverActivity = new ReceiverNotificationService();
        IntentFilter filter = new IntentFilter(SEND_SMS_INTENT_NOTIFICATION_ACTIVITY);
        registerReceiver(receiverActivity, filter);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Bundle extras = null;
        if (intent != null) {
            extras = intent.getExtras();
        }

        if (extras != null) {
            String notificationJson = extras.getString(CREATED_NOTIFICATION);
            String friendListJson = extras.getString(FRIENDS_LIST);
            if (notificationJson != null && friendListJson != null) {
                Gson gson = new Gson();
                NotificationData notificationData =
                        gson.fromJson(notificationJson, NotificationData.class);
                List<FriendData> friendData = restoreFriendList(friendListJson);

                boolean isBefore = Calendar.getInstance().getTime()
                        .before(notificationData.getDate());
                boolean needSchedule = isBefore && !notificationData.isScheduled();
                if (needSchedule) {
                    sendNotificationToGroup(this, friendData, notificationData);
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverActivity);
    }

    private void sendNotificationToGroup(final Context context, final List<FriendData> friendData,
                                         final NotificationData notificationData) {

        for (final FriendData friend : friendData) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    sendSms(context, friend.getPhone(), notificationData.getMessage());
                }
            }, notificationData.getDate());
        }
    }

    private void sendSms(final Context context, String to, String message) {
        new SendSmsHttpRequestTask(context).execute(createSendSmsParams(to, message));
    }

    private RestApiParams createSendSmsParams(String to, String message) {
        RestApiParams params = new RestApiParams();
        params.setUri(SEND_SMS_API_URI);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("to", to);
        queryParams.put("from", getMsisdn());
        queryParams.put("msg", message);
        queryParams.put("apikey", API_KEY);
        params.setQueryParams(queryParams);

        return params;
    }

    @SuppressLint("MissingPermission")
    private String getMsisdn() {
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return POLISH_AREA_CODE + telephonyManager.getLine1Number();
    }

    private List<FriendData> restoreFriendList(String friendsToRestore) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<FriendData>>() {
        }.getType();
        return gson.fromJson(friendsToRestore, listType);
    }
}

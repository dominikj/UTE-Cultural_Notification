package pl.ute.culturaltip.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.activity.NotificationActivity;
import pl.ute.culturaltip.api.orange.sms.SendSmsResponse;
import pl.ute.culturaltip.enums.NotificationStatus;

import static pl.ute.culturaltip.constants.Constants.Message.SEND_SMS_RESPONSE;

/**
 * Created by dominik on 11.02.18.
 */

public class ReceiverNotificationService extends BroadcastReceiver {

    private static final String DELIVERED_TO_NETWORK = "DeliveredToNetwork";
    private static final String OK_RESULT = "OK";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Received data", "received");
        NotificationActivity activity = (NotificationActivity) context;
        Gson gson = new Gson();
        SendSmsResponse sendSmsResponse =
                gson.fromJson(intent.getStringExtra(SEND_SMS_RESPONSE), SendSmsResponse.class);

        boolean isDeliveredToNetwork =
                DELIVERED_TO_NETWORK.equals(sendSmsResponse.getDeliveryStatus());
        boolean isResultOk = OK_RESULT.equals(sendSmsResponse.getResult());

        if (isDeliveredToNetwork && isResultOk) {
            activity.setNotificationStatus(NotificationStatus.SENT_OK);
            showNotification(context);
            return;
        }

        activity.setNotificationStatus(NotificationStatus.SENT_ERROR);
        showNotification(context);

    }

//        TODO On-screen notifications
    private void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(), PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle("NOTIFICATION")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("NOTI"))
                        .setContentText("NOTI")
                        .setPriority(Notification.PRIORITY_MAX);

        builder.setContentIntent(contentIntent);
        notificationManager.notify((int) 666, builder.build());
    }
}

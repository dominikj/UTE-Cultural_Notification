package pl.ute.culturaltip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

import pl.ute.culturaltip.activity.CreateMessageActivity;
import pl.ute.culturaltip.activity.NotificationActivity;
import pl.ute.culturaltip.api.orange.sms.SendSmsResponse;
import pl.ute.culturaltip.api.wikipedia.extracts.ExtractsArticleResponse;
import pl.ute.culturaltip.enums.NotificationStatus;

import static pl.ute.culturaltip.constants.Constants.Extracts.EXTRACTS_RESPONSE;
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
            return;
        }

        activity.setNotificationStatus(NotificationStatus.SENT_ERROR);


    }
}

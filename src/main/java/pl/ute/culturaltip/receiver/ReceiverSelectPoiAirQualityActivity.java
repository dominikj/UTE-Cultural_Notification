package pl.ute.culturaltip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pl.ute.culturaltip.activity.NotificationActivity;
import pl.ute.culturaltip.activity.SelectPoiActivity;
import pl.ute.culturaltip.constants.Constants;

/**
 * Created by dominik on 11.02.18.
 */

public class ReceiverSelectPoiAirQualityActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Received data", "received");
        SelectPoiActivity activity = (SelectPoiActivity) context;

        int airQuality = intent.getIntExtra(Constants.AirQuality.AIR_QUALITY, 0);
        activity.setAirQuality(airQuality);
    }
}

package pl.ute.culturaltip.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import pl.ute.culturaltip.constants.Constants;

import static pl.ute.culturaltip.R.id.position_text;
import static pl.ute.culturaltip.R.id.show_map_btn;
import static pl.ute.culturaltip.R.string;

/**
 * Created by dominik on 2016-05-06.
 */
public class ReceiverMainActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TextView positionText = (TextView) ((Activity) context).findViewById(position_text);
        String latitude = "", longtitude = "";

        if (intent.getExtras() != null) {
            latitude = intent.getExtras().getString(Constants.Location.LATITUDE);
            longtitude = intent.getExtras().getString(Constants.Location.LONGITUDE);
            positionText.setText("Service is currently unavailable");
            positionText.setError("");
            return;
        }
        positionText.setText(context.getString(string.position_data, latitude, longtitude));
        (((Activity) context).findViewById(show_map_btn)).setEnabled(true);
    }
}

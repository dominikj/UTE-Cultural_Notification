package pl.ute.culturaltip;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.example.dominik.ute.R;

import pl.ute.culturaltip.constants.Constants;

/**
 * Created by dominik on 2016-05-06.
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TextView positionText = (TextView) ((Activity) context).findViewById(R.id.position_text);
        String latitude = intent.getExtras().getString(Constants.Location.LATITUDE);
        String longtitude = intent.getExtras().getString(Constants.Location.LONGITUDE);
        positionText.setText(context.getString(R.string.position_data, latitude, longtitude));
        ((Button) ((Activity) context).findViewById(R.id.show_map_btn)).setEnabled(true);
    }
}

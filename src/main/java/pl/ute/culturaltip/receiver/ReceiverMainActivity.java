package pl.ute.culturaltip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.activity.MainActivity;
import pl.ute.culturaltip.constants.Constants;

import static pl.ute.culturaltip.R.id.show_map_btn;
import static pl.ute.culturaltip.constants.Constants.Location.DESCRIPTION;

/**
 * Created by dominik on 2016-05-06.
 */
public class ReceiverMainActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity activity = (MainActivity) context;

        TextView listTitleView = (TextView) activity.findViewById(R.id.list_title);

        if (intent.getExtras() == null) {
            listTitleView.setError("Service is currently unavailable");
            return;
        }
        String errorDescription = intent.getExtras().getString(DESCRIPTION);
        if (errorDescription != null) {
            listTitleView.setError(errorDescription);
            return;
        }

        String latitude = intent.getExtras().getString(Constants.Location.LATITUDE);
        String longtitude = intent.getExtras().getString(Constants.Location.LONGITUDE);
        activity.setLocationForCurrentSelectedFriend(latitude, longtitude);
        (activity.findViewById(show_map_btn)).setEnabled(true);
    }
}

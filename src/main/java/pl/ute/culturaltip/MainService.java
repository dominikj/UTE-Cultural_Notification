package pl.ute.culturaltip;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.ute.culturaltip.api.orange.LocationHttpRequestTask;
import pl.ute.culturaltip.restapiutils.RestApiParams;

/**
 * Created by dominik on 2016-05-06.
 */

public class MainService extends IntentService {

    private static final int HALF_MINUTE_IN_MILISECONDS = 30000;
    private static final int DEFAULT_INTERVAL = 1;
    private static final String API_KEY = "rIeprPWnQZRXtGO9uvzAZolOPIUGZ77L";
    private static final String LOCATION_API_URI =
            "https://apitest.orange.pl/Localization/v1/GeoLocation";
    private static final String POLISH_AREA_CODE = "48";


    public MainService() {
        super("MainService");
//        sheduleRefreshingLocation(DEFAULT_INTERVAL, this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        refreshLocation(this);

        Bundle extra = intent.getExtras();
        if (extra != null) {
//            if (extra.getString(MainActivity.TIME) != null)
//                interval = extra.getString(MainActivity.TIME);

        }
    }

    private void sheduleRefreshingLocation(final int intervalInMinutes, final Context context) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                refreshLocation(context);
            }
        }, 0, intervalInMinutes * HALF_MINUTE_IN_MILISECONDS);
    }

    private void refreshLocation(final Context context) {
        new LocationHttpRequestTask(context).execute(createLocationParams());
    }

    private RestApiParams createLocationParams() {
        RestApiParams params = new RestApiParams();
        params.setUri(LOCATION_API_URI);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("msisdn", getMsisdn());
        queryParams.put("apikey", API_KEY);
        params.setQueryParams(queryParams);

        return params;
    }

    @SuppressLint("MissingPermission")
    private String getMsisdn() {
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return POLISH_AREA_CODE + "511853957";
        //TODO - no number for sim 2
//        return POLISH_AREA_CODE + telephonyManager.getLine1Number();
    }
}


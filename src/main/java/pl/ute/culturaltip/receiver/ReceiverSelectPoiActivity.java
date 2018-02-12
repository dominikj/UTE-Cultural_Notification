package pl.ute.culturaltip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.ute.culturaltip.activity.SelectPoiActivity;
import pl.ute.culturaltip.api.google.PoiResponse;
import pl.ute.culturaltip.api.google.PoiResponseResult;

import static pl.ute.culturaltip.constants.Constants.Poi.POI_RESPONSE;

/**
 * Created by dominik on 10.02.18.
 */

public class ReceiverSelectPoiActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Received data", "received");
        SelectPoiActivity activity = (SelectPoiActivity) context;
        Gson gson = new Gson();
        PoiResponse poiResponse =
                gson.fromJson(intent.getStringExtra(POI_RESPONSE), PoiResponse.class);

        if (poiResponse.getResults() == null || poiResponse.getResults().isEmpty()) {
            List<String> errorItems = Collections.singletonList("We are sorry, but no results found");

            if (poiResponse.getErrorMessage() != null) {
                errorItems.add(poiResponse.getErrorMessage());
            }
            
            activity.getPoiListFragment().setItemsList(errorItems);
            activity.disableShowMapAndForwardButtons();
            return;
        }
        activity.setPois(poiResponse.getResults());
        activity.getPoiListFragment().setItemsList(createItemsList(poiResponse));
        activity.enableShowMapAndForwardButtons();
    }

    List<String> createItemsList(PoiResponse response) {
        List<String> items = new ArrayList<>();
        for (PoiResponseResult result : response.getResults()) {
            items.add(result.getName());
        }
        return items;
    }
}

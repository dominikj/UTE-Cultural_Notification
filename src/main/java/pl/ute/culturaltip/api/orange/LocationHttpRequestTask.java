package pl.ute.culturaltip.api.orange;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.enums.NotificationStatus;
import pl.ute.culturaltip.restapiutils.QueryBuilder;
import pl.ute.culturaltip.restapiutils.RestApiParams;

/**
 * Created by dominik on 05.02.18.
 */
public class LocationHttpRequestTask extends AsyncTask<RestApiParams, Object, LocationResponse> {

    private Context context;

    public LocationHttpRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected LocationResponse doInBackground(RestApiParams... params) {
        if (params.length == 0) {
            throw new RuntimeException("No parameters for async task");
        }

        try {
            String query = QueryBuilder.buildQuery(params[0]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(query, LocationResponse.class);
        } catch (Exception e) {
            Log.e("Async task exception:", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(LocationResponse answer) {

        if (answer == null) {
            return;
        }

        Intent intent = new Intent(Constants.IntentCode.LOCATION_INTENT_MAIN_ACTIVITY);
        intent.putExtra(Constants.Location.LATITUDE, answer.getLatitude());
        intent.putExtra(Constants.Location.LONGITUDE, answer.getLongitude());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.i("Location - latitude", answer.getLatitude());
        Log.i("Location - longitude", answer.getLongitude());

        context.sendBroadcast(intent);
    }
}

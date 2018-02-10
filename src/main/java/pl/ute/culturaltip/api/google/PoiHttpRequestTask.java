package pl.ute.culturaltip.api.google;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.restapiutils.QueryBuilder;
import pl.ute.culturaltip.restapiutils.RestApiParams;

/**
 * Created by dominik on 05.02.18.
 */
public class PoiHttpRequestTask extends AsyncTask<RestApiParams, Object, PoiResponse> {

    private Context context;

    public PoiHttpRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected PoiResponse doInBackground(RestApiParams... params) {
        if (params.length == 0) {
            throw new RuntimeException("No parameters for async task");
        }

        try {
            String query = QueryBuilder.buildQuery(params[0]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(query, PoiResponse.class);
        } catch (Exception e) {
            Log.e("Async task exception:", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(PoiResponse answer) {

        Intent intent = new Intent(Constants.IntentCode.POI_INTENT_SELECT_POI_ACTIVITY);
        if (answer == null) {
            context.sendBroadcast(intent);
            return;
        }
        Gson gson = new Gson();
        intent.putExtra(Constants.Poi.POI_RESPONSE, gson.toJson(answer));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.i("Poi records", gson.toJson(answer));

        context.sendBroadcast(intent);
    }
}

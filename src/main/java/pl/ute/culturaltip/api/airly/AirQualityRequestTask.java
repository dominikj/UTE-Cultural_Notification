package pl.ute.culturaltip.api.airly;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.restapiutils.QueryBuilder;
import pl.ute.culturaltip.restapiutils.RestApiParams;

import static java.util.Collections.*;

/**
 * Created by dominik on 05.02.18.
 */
public class AirQualityRequestTask extends AsyncTask<RestApiParams, Object, AirQualityResponse> {

    private Context context;

    public AirQualityRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected AirQualityResponse doInBackground(RestApiParams... params) {
        if (params.length == 0) {
            throw new RuntimeException("No parameters for async task");
        }

        try {
            String query = QueryBuilder.buildQuery(params[0]);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
            headers.add("apikey", Constants.ApiKey.API_KEY_AIRLY);
            HttpEntity entity = new HttpEntity<>(headers);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            return restTemplate.exchange(query, HttpMethod.GET, entity,
                    AirQualityResponse.class).getBody();
        } catch (Exception e) {
            Log.e("Async task exception:", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(AirQualityResponse answer) {

        Intent intent = new Intent(Constants.IntentCode.AIR_QUALITY_INTENT_SELECT_POI_ACTIVITY);
        if (answer == null) {
            context.sendBroadcast(intent);
            return;
        }

        intent.putExtra(Constants.AirQuality.AIR_QUALITY, answer.getPollutionLevel());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.i("Pollution level:", String.valueOf(answer.getPollutionLevel()));

        context.sendBroadcast(intent);
    }
}

package pl.ute.culturaltip.api.wikipedia.extracts;

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

import static pl.ute.culturaltip.constants.Constants.IntentCode.MESSAGE_INTENT_CREATE_MESSAGE_ACTIVITY;

/**
 * Created by dominik on 05.02.18.
 */
public class ExtractsArticleHttpRequestTask extends AsyncTask<RestApiParams, Object, ExtractsArticleResponse> {

    private Context context;

    public ExtractsArticleHttpRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected ExtractsArticleResponse doInBackground(RestApiParams... params) {
        if (params.length == 0) {
            throw new RuntimeException("No parameters for async task");
        }

        try {
            String query = QueryBuilder.buildQuery(params[0]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(query, ExtractsArticleResponse.class);
        } catch (Exception e) {
            Log.e("Async task exception:", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(ExtractsArticleResponse answer) {

        Intent intent = new Intent(MESSAGE_INTENT_CREATE_MESSAGE_ACTIVITY);
        if (answer == null) {
            context.sendBroadcast(intent);
            return;
        }
        Gson gson = new Gson();
        intent.putExtra(Constants.Extracts.EXTRACTS_RESPONSE, gson.toJson(answer));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.i("Extracts", gson.toJson(answer));

        context.sendBroadcast(intent);
    }
}

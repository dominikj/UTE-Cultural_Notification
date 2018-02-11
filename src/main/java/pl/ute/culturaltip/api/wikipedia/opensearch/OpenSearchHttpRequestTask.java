package pl.ute.culturaltip.api.wikipedia.opensearch;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import pl.ute.culturaltip.api.orange.LocationResponse;
import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.restapiutils.QueryBuilder;
import pl.ute.culturaltip.restapiutils.RestApiParams;

import static pl.ute.culturaltip.constants.Constants.IntentCode.ARTICLE_SEARCH_INTENT_SELECT_ARTICLE_ACTIVITY;

/**
 * Created by dominik on 05.02.18.
 */
public class OpenSearchHttpRequestTask extends AsyncTask<RestApiParams, Object, OpenSearchResponse> {

    private Context context;

    public OpenSearchHttpRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected OpenSearchResponse doInBackground(RestApiParams... params) {
        if (params.length == 0) {
            throw new RuntimeException("No parameters for async task");
        }

        try {
            String query = QueryBuilder.buildQuery(params[0]);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            return restTemplate.getForObject(query, OpenSearchResponse.class);
        } catch (Exception e) {
            Log.e("Async task exception:", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(OpenSearchResponse answer) {

        Intent intent = new Intent(ARTICLE_SEARCH_INTENT_SELECT_ARTICLE_ACTIVITY);
        if (answer == null) {
            context.sendBroadcast(intent);
            return;
        }
        Gson gson = new Gson();
        intent.putExtra(Constants.Article.ARTICLE_RESPONSE, gson.toJson(answer));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.i("Article records", gson.toJson(answer));

        context.sendBroadcast(intent);
    }
}

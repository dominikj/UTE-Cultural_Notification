package pl.ute.culturaltip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.ute.culturaltip.activity.SelectArticleActivity;
import pl.ute.culturaltip.api.wikipedia.opensearch.OpenSearchItem;
import pl.ute.culturaltip.api.wikipedia.opensearch.OpenSearchResponse;

import static pl.ute.culturaltip.constants.Constants.Article.ARTICLE_RESPONSE;

/**
 * Created by dominik on 10.02.18.
 */

public class ReceiverSelectArticleActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Received data", "received");
        SelectArticleActivity activity = (SelectArticleActivity) context;
        Gson gson = new Gson();
        OpenSearchResponse articleResponse =
                gson.fromJson(intent.getStringExtra(ARTICLE_RESPONSE), OpenSearchResponse.class);

        if (articleResponse == null || articleResponse.getQuery().getSearch().isEmpty()) {
            activity.getArticleListFragment().setItemsList(Arrays.asList("We are sorry, but no results found"));
            return;
        }
        activity.setArticles(articleResponse.getQuery().getSearch());
        activity.getArticleListFragment().setItemsList(createItemsList(articleResponse));
        activity.enableForwardButton();
    }

    List<String> createItemsList(OpenSearchResponse response) {
        List<String> items = new ArrayList<>();
        for (OpenSearchItem result : response.getQuery().getSearch()) {
            items.add(result.getTitle());
        }
        return items;
    }
}

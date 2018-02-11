package pl.ute.culturaltip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import pl.ute.culturaltip.activity.CreateMessageActivity;
import pl.ute.culturaltip.api.wikipedia.extracts.ExtractsArticlePage;
import pl.ute.culturaltip.api.wikipedia.extracts.ExtractsArticleResponse;

import static pl.ute.culturaltip.constants.Constants.Extracts.EXTRACTS_RESPONSE;

/**
 * Created by dominik on 10.02.18.
 */

public class ReceiverCreateMessageActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Received data", "received");
        CreateMessageActivity activity = (CreateMessageActivity) context;
        Gson gson = new Gson();
        ExtractsArticleResponse articleResponse =
                gson.fromJson(intent.getStringExtra(EXTRACTS_RESPONSE), ExtractsArticleResponse.class);

        if (articleResponse == null || articleResponse.getQuery().getPages() == null) {
            activity.showToast("We are sorry, but no results found");
            return;
        }

        Map<String, Object> rawMap = (Map<String, Object>) articleResponse.getQuery().getPages();
        String message = (String) ((Map) rawMap.values().toArray()[0]).get("extract");
        activity.setMessage(message);
        activity.enableForwardButton();
    }
}

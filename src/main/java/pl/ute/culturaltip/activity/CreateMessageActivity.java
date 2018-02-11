package pl.ute.culturaltip.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.api.wikipedia.extracts.ExtractsArticleHttpRequestTask;
import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.receiver.ReceiverCreateMessageActivity;
import pl.ute.culturaltip.restapiutils.RestApiParams;

import static pl.ute.culturaltip.constants.Constants.Article.NAME_OF_SELECTED_ARTICLE;
import static pl.ute.culturaltip.constants.Constants.IntentCode.MESSAGE_INTENT_CREATE_MESSAGE_ACTIVITY;

/**
 * Created by dominik on 11.02.18.
 */

public class CreateMessageActivity extends AbstractNavigationActivity {

    private static final String EXTRACTS_ARTICLE_API = "https://pl.wikipedia.org/w/api.php";

    private String message;
    private TextView messageText;
    private String nameOfSelectedArticle;

    private BroadcastReceiver receiverActivity;

    public CreateMessageActivity() {
        super(R.id.create_message_btn, R.id.back_create_message_btn);

    }

    @Override
    protected Intent createIntentForForward() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        receiverActivity = new ReceiverCreateMessageActivity();
        IntentFilter filter = new IntentFilter(MESSAGE_INTENT_CREATE_MESSAGE_ACTIVITY);
        registerReceiver(receiverActivity, filter);
    }

    @Override
    protected void onResume() {
        boolean isOwnMessage = getIntent().getExtras().getBoolean(Constants.Article.OWN_MESSAGE);
        super.onResume(isOwnMessage, true);
        messageText = (TextView) findViewById(R.id.message_text);

        if (getIntent().getExtras() != null) {
            nameOfSelectedArticle = getIntent().getExtras().getString(NAME_OF_SELECTED_ARTICLE);
        }

        if (!isOwnMessage) {
            new ExtractsArticleHttpRequestTask(this)
                    .execute(createExtractsArticleParams(nameOfSelectedArticle));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverActivity);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        messageText.setText(message);
    }

    private RestApiParams createExtractsArticleParams(String articleName) {
        RestApiParams params = new RestApiParams();
        params.setUri(EXTRACTS_ARTICLE_API);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("action", "query");
        queryParams.put("prop", "extracts");
        queryParams.put("exintro", "");
        queryParams.put("explaintext", "");
        queryParams.put("titles", articleName);
        queryParams.put("format", "json");
        params.setQueryParams(queryParams);

        return params;
    }

}

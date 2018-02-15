package pl.ute.culturaltip.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.api.wikipedia.extracts.ExtractsArticleHttpRequestTask;
import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.receiver.ReceiverCreateMessageActivity;
import pl.ute.culturaltip.restapiutils.RestApiParams;

import static pl.ute.culturaltip.constants.Constants.ApiUri.ApiWikipedia.WIKIPEDIA_API_URI;
import static pl.ute.culturaltip.constants.Constants.Article.NAME_OF_SELECTED_ARTICLE;
import static pl.ute.culturaltip.constants.Constants.IntentCode.MESSAGE_INTENT_CREATE_MESSAGE_ACTIVITY;
import static pl.ute.culturaltip.constants.Constants.Message.CREATED_MESSAGE;

/**
 * Created by dominik on 11.02.18.
 */

public class CreateMessageActivity extends AbstractNavigationActivity {

    private static final int MAX_SMS_LENGTH = 160;

    private String message;
    private TextView messageText;
    private String nameOfSelectedArticle;

    private BroadcastReceiver receiverActivity;

    public CreateMessageActivity() {
        super(R.id.forward_create_message_btn, R.id.back_create_message_btn);

    }

    @Override
    protected Intent createIntentForForward() {
        if (message == null) {
            message = messageText.getText().toString();
        }

        if (message.length() > MAX_SMS_LENGTH) {
            messageText.setError(getString(R.string.message_is_too_long));
            return null;
        }
        Intent intent = new Intent(getContext(), SetTimeNotificationActivity.class);
        intent.putExtra(CREATED_MESSAGE, message);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        receiverActivity = new ReceiverCreateMessageActivity();
        IntentFilter filter = new IntentFilter(MESSAGE_INTENT_CREATE_MESSAGE_ACTIVITY);
        registerReceiver(receiverActivity, filter);

        if (savedInstanceState != null) {
            message = savedInstanceState.getString(Constants.Message.CREATED_MESSAGE);
        }

    }

    @Override
    protected void onResume() {
        boolean isOwnMessage = getIntent().getExtras().getBoolean(Constants.Message.IS_OWN_MESSAGE);
        super.onResume(isOwnMessage, true);
        messageText = (TextView) findViewById(R.id.message_text);

        if (getIntent().getExtras() != null) {
            nameOfSelectedArticle = getIntent().getExtras().getString(NAME_OF_SELECTED_ARTICLE);
        }

        if (isOwnMessage) {
            messageText.setText(nameOfSelectedArticle);
            return;
        }
        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NOP
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //NOP
            }

            @Override
            public void afterTextChanged(Editable s) {
                message = s.toString();
            }
        });

        if (message == null) {
            new ExtractsArticleHttpRequestTask(this)
                    .execute(createExtractsArticleParams(nameOfSelectedArticle));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.Message.CREATED_MESSAGE, message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverActivity);

    }

    public void setMessage(String message) {
        this.message = message;
        messageText.setText(message);
    }

    private RestApiParams createExtractsArticleParams(String articleName) {
        RestApiParams params = new RestApiParams();
        params.setUri(WIKIPEDIA_API_URI);

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

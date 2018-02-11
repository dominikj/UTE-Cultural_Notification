package pl.ute.culturaltip.activity;

import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.api.wikipedia.opensearch.OpenSearchHttpRequestTask;
import pl.ute.culturaltip.api.wikipedia.opensearch.OpenSearchItem;
import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.fragment.DefaultListFragment;
import pl.ute.culturaltip.receiver.ReceiverSelectArticleActivity;
import pl.ute.culturaltip.restapiutils.RestApiParams;

/**
 * Created by dominik on 11.02.18.
 */

public class SelectArticleActivity extends AbstractAsynchronousListActivity {
    private static final String OPENSEARCH_API = "https://pl.wikipedia.org/w/api.php";
    private String nameOfSelectedPoi = "";

    public SelectArticleActivity() {
        super(R.id.forward_select_article_btn, R.id.back_select_article_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_article);

        setIntentReceiver(new ReceiverSelectArticleActivity(),
                Constants.IntentCode.ARTICLE_SEARCH_INTENT_SELECT_ARTICLE_ACTIVITY);
    }

    @Override
    protected void onResume() {
        super.onResume(false, true);
        setListFragment((DefaultListFragment) getFragmentManager()
                .findFragmentById(R.id.article_list));
        getListFragment().setListName(getString(R.string.article_list));
        nameOfSelectedPoi = getIntent().getExtras().getString(Constants.Poi.NAME_OF_SELECTED_POI);

        new OpenSearchHttpRequestTask(this).execute(createSearchArticleParams(nameOfSelectedPoi));
    }


    @Override
    protected Intent createIntentForForward() {
        return null;
    }

    public List<OpenSearchItem> getArticles() {
        return getListElements();
    }

    public void setArticles(List<OpenSearchItem> articles) {
        setListElements(articles);
    }

    public DefaultListFragment getArticleListFragment() {
        return getListFragment();
    }

    public void enableForwardButton() {
        getForwardButton().setEnabled(true);
    }

    private RestApiParams createSearchArticleParams(String query) {
        RestApiParams params = new RestApiParams();
        params.setUri(OPENSEARCH_API);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("action", "query");
        queryParams.put("list", "search");
        queryParams.put("srsearch", query);
        queryParams.put("format", "json");
        params.setQueryParams(queryParams);

        return params;
    }
}

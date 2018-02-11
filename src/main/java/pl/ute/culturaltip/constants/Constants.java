package pl.ute.culturaltip.constants;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

/**
 * Created by dominik on 05.02.18.
 */
public interface Constants {

    interface Location {
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }

    interface Poi {
        String POI_RESPONSE = "poiResponse";
        String NAME_OF_SELECTED_POI = "nameOfSelectedPoi";
    }

    interface Article {
        String ARTICLE_RESPONSE = "articleResponse";
        String NAME_OF_SELECTED_ARTICLE = "nameOfSelectedArticle";
        String OWN_MESSAGE = "ownMessage";
    }

    interface Extracts {
        String EXTRACTS_RESPONSE = "extractsResponse";
    }

    interface IntentCode {
        String LOCATION_INTENT_MAIN_ACTIVITY = "pl.ute.culturaltip.mainActivity.location";
        String POI_INTENT_SELECT_POI_ACTIVITY = "pl.ute.culturaltip.selectPoiActivity.poiList";
        String MESSAGE_INTENT_CREATE_MESSAGE_ACTIVITY =
                "pl.ute.culturaltip.createMessageActivity.message";
        String ARTICLE_SEARCH_INTENT_SELECT_ARTICLE_ACTIVITY =
                "pl.ute.culturaltip.selectArticleActivity.articleList";
    }

    interface Friend {
        String FRIEND_NAME = "friendName";
        String FRIEND_PHONE = "friendPhone";
    }

    interface Permission {
        int REQUEST_READ_PHONE_PERMISSIONS = 1;
        String[] REQUEST_PERMISSIONS = {READ_PHONE_STATE, INTERNET, ACCESS_COARSE_LOCATION,
                READ_SMS, ACCESS_FINE_LOCATION};
    }
}

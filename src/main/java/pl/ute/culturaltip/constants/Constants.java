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
        String DESCRIPTION = "description";

        interface Gps {
            int MIN_UPDATE_TIME = 60000;
            int MIN_DISTANCE_CHANGE = 10;
        }
    }

    interface Poi {
        String POI_RESPONSE = "poiResponse";
        String NAME_OF_SELECTED_POI = "nameOfSelectedPoi";
    }

    interface Article {
        String ARTICLE_RESPONSE = "articleResponse";
        String NAME_OF_SELECTED_ARTICLE = "nameOfSelectedArticle";
    }

    interface Message {
        String SEND_SMS_RESPONSE = "sendSmsResponse";
        String IS_OWN_MESSAGE = "ownMessage";
        String CREATED_MESSAGE = "createdMessage";

    }

    interface Extracts {
        String EXTRACTS_RESPONSE = "extractsResponse";
    }

    interface IntentCode {
        String LOCATION_INTENT_MAIN_ACTIVITY = "pl.ute.culturaltip.mainActivity.location";
        String POI_INTENT_SELECT_POI_ACTIVITY = "pl.ute.culturaltip.selectPoiActivity.poiList";
        String SEND_SMS_INTENT_NOTIFICATION_ACTIVITY =
                "pl.ute.culturaltip.notificationActivity.sendSmsResponse";
        String MESSAGE_INTENT_CREATE_MESSAGE_ACTIVITY =
                "pl.ute.culturaltip.createMessageActivity.message";
        String ARTICLE_SEARCH_INTENT_SELECT_ARTICLE_ACTIVITY =
                "pl.ute.culturaltip.selectArticleActivity.articleList";
    }

    interface Friend {
        String FRIEND_NAME = "friendName";
        String FRIEND_PHONE = "friendPhone";
        String FRIENDS_PREFERENCIES_KEY = "friends";
        String FRIENDS_LIST = "friendsList";
    }

    interface Notification {
        String CREATED_NOTIFICATION = "createdNotification";
    }

    interface Permission {
        int REQUEST_READ_PHONE_PERMISSIONS = 1;
        String[] REQUEST_PERMISSIONS = {READ_PHONE_STATE, INTERNET, ACCESS_COARSE_LOCATION,
                READ_SMS, ACCESS_FINE_LOCATION};
    }

    interface ApiKey {
        String API_KEY_ORANGE = "rIeprPWnQZRXtGO9uvzAZolOPIUGZ77L";
        //        String API_KEY_GOOGLE = "AIzaSyBeZxPitp7UPkyDzYPS1rpLSMvObNcmA-Q";
        String API_KEY_GOOGLE = "AIzaSyB3L5EKLJhxgz0zu5TAJ2nHG0nwyVktGoE";
    }

    interface ApiUri {
        interface ApiOrange {
            String LOCATION_API_URI = "https://apitest.orange.pl/Localization/v1/GeoLocation";
            String SEND_SMS_API_URI = "https://apitest.orange.pl/Messaging/v1/SMSOnnet";
            String NEARBY_SEARCH_API_URI =
                    "https://maps.googleapis.com/maps/api/place/textsearch/json";
            String WIKIPEDIA_API_URI = "https://pl.wikipedia.org/w/api.php";

        }
    }
}

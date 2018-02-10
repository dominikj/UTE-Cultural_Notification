package pl.ute.culturaltip.constants;

/**
 * Created by dominik on 05.02.18.
 */
public interface Constants {
    String API_TYPE = "apiType";

    interface Location {
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }

    interface IntentCode {
        String LOCATION_INTENT_MAIN_ACTIVITY = "pl.ute.culturaltip.mainActivity.location";
    }

    interface Friend {
        String FRIEND_NAME = "friendName";
        String FRIEND_PHONE = "friendPhone";
    }
}

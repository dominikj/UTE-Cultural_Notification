package pl.ute.culturaltip.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.api.orange.location.LocationHttpRequestTask;
import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.constants.Constants.Location;
import pl.ute.culturaltip.data.FriendData;
import pl.ute.culturaltip.fragment.ListenableListFragment;
import pl.ute.culturaltip.receiver.ReceiverMainActivity;
import pl.ute.culturaltip.restapiutils.RestApiParams;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.widget.AdapterView.INVALID_POSITION;
import static pl.ute.culturaltip.R.id.position_text;
import static pl.ute.culturaltip.constants.Constants.ApiKey.API_KEY_ORANGE;
import static pl.ute.culturaltip.constants.Constants.ApiUri.ApiOrange.LOCATION_API_URI;
import static pl.ute.culturaltip.constants.Constants.Friend.FRIENDS_PREFERENCIES_KEY;
import static pl.ute.culturaltip.constants.Constants.Friend.FRIEND_NAME;
import static pl.ute.culturaltip.constants.Constants.Friend.FRIEND_PHONE;
import static pl.ute.culturaltip.constants.Constants.Location.Gps.MIN_DISTANCE_CHANGE;
import static pl.ute.culturaltip.constants.Constants.Location.Gps.MIN_UPDATE_TIME;
import static pl.ute.culturaltip.constants.Constants.Permission.REQUEST_PERMISSIONS;
import static pl.ute.culturaltip.constants.Constants.Permission.REQUEST_READ_PHONE_PERMISSIONS;


public class MainActivity extends AppCompatActivity implements LocationListener, AsynchronousListListener {
    private static final int REQUEST_CODE = 1;

    private List<FriendData> friends = new ArrayList<>();
    private ListenableListFragment friendFragment;
    private ReceiverMainActivity receiverMainActivity;
    private Button showMapButton;
    private LocationManager locationManager;
    private double myCurrentLatitude;
    private double myCurrentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserPermissions();

        setContentView(R.layout.activity_main);
        receiverMainActivity = new ReceiverMainActivity();
        IntentFilter filter = new IntentFilter(Constants.IntentCode.LOCATION_INTENT_MAIN_ACTIVITY);
        registerReceiver(receiverMainActivity, filter);

        friendFragment = (ListenableListFragment) getFragmentManager()
                .findFragmentById(R.id.friends_list);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String friends = settings.getString(FRIENDS_PREFERENCIES_KEY, "");
        if (!friends.trim().isEmpty()) {
            restoreFriendList(friends);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        friendFragment.setListName(getString(R.string.friends_list));
        showMapButton = (Button) findViewById(R.id.show_map_btn);
        Button addFriendButton = (Button) findViewById(R.id.add_friend_btn);
        Button removeFriendButton = (Button) findViewById(R.id.remove_friend_btn);
        Button notificationButton = (Button) findViewById(R.id.show_notification_btn);
        Button showMeOnMapButton = (Button) findViewById(R.id.show_me_on_map_btn);

        if (addFriendButton != null) {
            addFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getContext(), AddFriendAcitivity.class),
                            REQUEST_CODE);
                }
            });
        }

        if (notificationButton != null) {
            notificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveFriendList();
                    startActivity(new Intent(getContext(), NotificationActivity.class));
                }
            });
        }

        if (removeFriendButton != null) {
            removeFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedPosition = friendFragment.getSelectedPosition();
                    if (isListFulfilledProperly()) {
                        MainActivity.this.friends.remove(selectedPosition);
                        friendFragment.setItemsList(buildFriendItemList(MainActivity.this.friends));
                    }
                }
            });
        }

        if (showMapButton != null) {
            showMapButton.setEnabled(false);
            showMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(createLocationIntent());
                }
            });
        }

        showMeOnMapButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (isPermissionsGranted()) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_DISTANCE_CHANGE,
                            (LocationListener) getContext());
                    onLocationChanged(
                            locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
                    startActivity(createMyLocationIntent(myCurrentLatitude, myCurrentLongitude));
                }
            }
        });
    }

    private boolean isPermissionsGranted() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveFriendList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverMainActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                FriendData friendData = new FriendData();
                friendData.setName(data.getStringExtra(FRIEND_NAME));
                friendData.setPhone(data.getStringExtra(FRIEND_PHONE));
                addToFriendList(friendData);
            }
        }
    }

    private void saveFriendList() {
        try {
            SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = settings.edit();
            Gson gson = new Gson();
            editor.putString(FRIENDS_PREFERENCIES_KEY, gson.toJson(friends));
            editor.apply();
        } catch (Exception e) {
            Log.e("Preferencies exception", e.getMessage(), e);

        }
    }

    private void restoreFriendList(String friendsToRestore) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<FriendData>>() {
        }.getType();
        this.friends = gson.fromJson(friendsToRestore, listType);
        setFriendList(this.friends);
    }

    private void setFriendList(List<FriendData> friends) {
        friendFragment.setItemsList(buildFriendItemList(friends));
    }

    private void addToFriendList(FriendData friend) {
        friends.add(friend);
        friendFragment.addItemToList(buildFriendItem(friend));
        Toast.makeText(getContext(), getString(R.string.friend_added_msg), Toast.LENGTH_SHORT)
                .show();

    }

    private void getUserPermissions() {
        if (!isPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS,
                    REQUEST_READ_PHONE_PERMISSIONS);
        }
    }

    private Context getContext() {
        return this;
    }

    @Nullable
    private Intent createLocationIntent() {
        FriendData friendData = friends.get(friendFragment.getSelectedPosition());

        if (friendData.getLatitude() == null || friendData.getLongitude() == null) {
            return null;
        }

        Intent intent = new Intent(getContext(), MapsActivity.class);
        intent.putExtra(Location.LATITUDE, getOnlyNumber(friendData.getLatitude()));
        intent.putExtra(Location.LONGITUDE, getOnlyNumber(friendData.getLongitude()));
        return intent;
    }

    private String getOnlyNumber(String coordinate) {
        return coordinate.substring(0, coordinate.length() - 1);
    }

    private List<String> buildFriendItemList(List<FriendData> friends) {
        List<String> items = new ArrayList<>();
        for (FriendData friend : friends) {
            items.add(buildFriendItem(friend));
        }
        return items;
    }

    private String buildFriendItem(FriendData friendData) {
        return friendData.getName().concat("(").concat(friendData.getPhone()).concat(")");
    }

    private RestApiParams createLocationParams(int selectedFriendPosition) {

        if (friends == null || selectedFriendPosition == INVALID_POSITION) {
            return null;
        }

        RestApiParams params = new RestApiParams();
        params.setUri(LOCATION_API_URI);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("msisdn", friends.get(selectedFriendPosition).getPhone());
        queryParams.put("apikey", API_KEY_ORANGE);
        params.setQueryParams(queryParams);

        return params;
    }

    private boolean isListFulfilledProperly() {
        return this.friends.size() != 0 && friendFragment.getSelectedPosition() != INVALID_POSITION;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        TextView positionText = (TextView) findViewById(position_text);

        this.myCurrentLatitude = location.getLatitude();
        this.myCurrentLongitude = location.getLongitude();
        positionText.setText(getString(R.string.position_data,
                String.valueOf(myCurrentLatitude),
                String.valueOf(myCurrentLongitude)));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //NOP
    }

    @Override
    public void onProviderEnabled(String provider) {
        //NOP
    }

    @Override
    public void onProviderDisabled(String provider) {
        //NOP
    }

    public void setLocationForCurrentSelectedFriend(String latitude, String longitude) {
        if (isListFulfilledProperly()) {
            FriendData friendData = this.friends.get(friendFragment.getSelectedPosition());
            friendData.setLatitude(latitude);
            friendData.setLongitude(longitude);
        }
    }

    private Intent createMyLocationIntent(double latitude, double longitude) {
        Intent intent = new Intent(getContext(), MapsActivity.class);
        intent.putExtra(Location.LATITUDE, String.valueOf(latitude));
        intent.putExtra(Location.LONGITUDE, String.valueOf(longitude));
        return intent;
    }

    @Override
    public void onSelectItem(int selectedPosition) {
        showMapButton.setEnabled(false);
        new LocationHttpRequestTask(this).execute(createLocationParams(selectedPosition));
    }
}


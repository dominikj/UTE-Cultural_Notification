package pl.ute.culturaltip.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import java.util.List;

import pl.ute.culturaltip.constants.Constants.Permission;
import pl.ute.culturaltip.service.MainService;
import pl.ute.culturaltip.R;
import pl.ute.culturaltip.receiver.ReceiverMainActivity;
import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.constants.Constants.Location;
import pl.ute.culturaltip.data.FriendData;
import pl.ute.culturaltip.fragment.DefaultListFragment;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static pl.ute.culturaltip.constants.Constants.Friend.FRIENDS_PREFERENCIES_KEY;
import static pl.ute.culturaltip.constants.Constants.Friend.FRIEND_NAME;
import static pl.ute.culturaltip.constants.Constants.Friend.FRIEND_PHONE;
import static pl.ute.culturaltip.constants.Constants.Permission.*;
import static pl.ute.culturaltip.constants.Constants.Permission.REQUEST_READ_PHONE_PERMISSIONS;
import static pl.ute.culturaltip.fragment.DefaultListFragment.NONE_SELECTED;


public class MainActivity extends AppCompatActivity {
    private static final int LATITUDE = 0;
    private static final int LONGITUDE = 1;
    private static final int REQUEST_CODE = 1;

    private List<FriendData> friends = new ArrayList<>();
    private DefaultListFragment friendFragment;
    private ReceiverMainActivity receiverMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserPermissions();

        setContentView(R.layout.activity_main);
        receiverMainActivity = new ReceiverMainActivity();
        IntentFilter filter = new IntentFilter(Constants.IntentCode.LOCATION_INTENT_MAIN_ACTIVITY);
        registerReceiver(receiverMainActivity, filter);

        friendFragment = (DefaultListFragment) getFragmentManager()
                .findFragmentById(R.id.friends_list);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String friends = settings.getString(FRIENDS_PREFERENCIES_KEY, "");
        if (!friends.trim().isEmpty()) {
            restoreFriendList(friends);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, MainService.class));
        friendFragment.setListName(getString(R.string.friends_list));

        Button showMapButton = (Button) findViewById(R.id.show_map_btn);
        Button addFriendButton = (Button) findViewById(R.id.add_friend_btn);
        Button removeFriendButton = (Button) findViewById(R.id.remove_friend_btn);
        Button notificationButton = (Button) findViewById(R.id.show_notification_btn);

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
                    if (MainActivity.this.friends.size() != 0 && selectedPosition != NONE_SELECTED) {
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
        int permissionCheck = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS,
                    REQUEST_READ_PHONE_PERMISSIONS);
        }
    }

    private Context getContext() {
        return this;
    }

    @Nullable
    private Intent createLocationIntent() {
        Intent intent = new Intent(getContext(), MapsActivity.class);
        TextView positionTextView = (TextView) findViewById(R.id.position_text);
        if (positionTextView != null) {
            String positionText = positionTextView.getText().toString();
            String[] coordinates = positionText.split(" : ");
            intent.putExtra(Location.LATITUDE, getOnlyNumber(coordinates[LATITUDE]));
            intent.putExtra(Location.LONGITUDE, getOnlyNumber(coordinates[LONGITUDE]));
            return intent;
        }
        return null;
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
}


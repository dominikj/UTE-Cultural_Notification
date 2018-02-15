package pl.ute.culturaltip.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.ute.culturaltip.R;
import pl.ute.culturaltip.api.airly.AirQualityRequestTask;
import pl.ute.culturaltip.api.google.PoiHttpRequestTask;
import pl.ute.culturaltip.api.google.PoiLocation;
import pl.ute.culturaltip.api.google.PoiResponseResult;
import pl.ute.culturaltip.constants.Constants;
import pl.ute.culturaltip.fragment.DefaultListFragment;
import pl.ute.culturaltip.receiver.ReceiverSelectPoiActivity;
import pl.ute.culturaltip.receiver.ReceiverSelectPoiAirQualityActivity;
import pl.ute.culturaltip.restapiutils.RestApiParams;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.widget.AdapterView.INVALID_POSITION;
import static pl.ute.culturaltip.constants.Constants.ApiKey.API_KEY_GOOGLE;
import static pl.ute.culturaltip.constants.Constants.ApiUri.ApiAirly.AIRLY_API_URI;
import static pl.ute.culturaltip.constants.Constants.ApiUri.ApiGoogle.NEARBY_SEARCH_API_URI;
import static pl.ute.culturaltip.constants.Constants.Location.Gps.MIN_DISTANCE_CHANGE;
import static pl.ute.culturaltip.constants.Constants.Location.Gps.MIN_UPDATE_TIME;
import static pl.ute.culturaltip.constants.Constants.Permission.REQUEST_PERMISSIONS;
import static pl.ute.culturaltip.constants.Constants.Permission.REQUEST_READ_PHONE_PERMISSIONS;
import static pl.ute.culturaltip.constants.Constants.Poi.NAME_OF_SELECTED_POI;

/**
 * Created by dominik on 10.02.18.
 */

public class SelectPoiActivity extends AbstractAsynchronousListActivity
        implements SeekBar.OnSeekBarChangeListener, LocationListener, AsynchronousListListener {

    private static final String AIR_QUALITY_GOOD = "good";
    private static final String AIR_QUALITY_MEDIUM = "medium";
    private static final String AIR_QUALITY_BAD = "bad";
    private static final int LOW_POLLUTION_LEVEL = 2;
    private static final int MEDIUM_POLLUTION_LEVEL = 4;
    private static final float MAX_STARS = 6;
    private static final int REQUEST_CODE = 1;
    private static final int PROGRESS_SCALE = 50;
    private static final int SEARCH_AIR_MEASUREMENT_DISTANCE = 1200;
    private static final int DEFAULT_RADIUS = 1000;
    private static final String SEARCH_TYPES =
            "art_gallery|cemetery|church|zoo|museum|movie_rental|movie_theater|library"
                    + "|park|painter|stadium|amusement_park|casino|shopping_mall";

    private int radius = DEFAULT_RADIUS;
    private TextView radiusView;
    private Button searchPoisButton;
    private Button showOnMapButton;
    private LocationManager locationManager;
    private boolean isButtonsEnabled;

    public SelectPoiActivity() {
        super(R.id.forward_select_poi_btn, R.id.back_select_poi_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_poi);
        getUserPermissions();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setListFragment((DefaultListFragment) getFragmentManager().findFragmentById(R.id.poi_list));

        searchPoisButton = (Button) findViewById(R.id.search_pois);

        setIntentReceiver(new ReceiverSelectPoiActivity(),
                Constants.IntentCode.POI_INTENT_SELECT_POI_ACTIVITY);

        setIntentReceiver(new ReceiverSelectPoiAirQualityActivity(),
                Constants.IntentCode.AIR_QUALITY_INTENT_SELECT_POI_ACTIVITY);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onResume() {
        super.onResume(isButtonsEnabled, true);
        showOnMapButton = (Button) findViewById(R.id.show_poi_on_map);
        radiusView = (TextView) findViewById(R.id.radius_text);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(DEFAULT_RADIUS / PROGRESS_SCALE);
        radiusView.setText(String.valueOf(DEFAULT_RADIUS));

        getListFragment().setListName(getString(R.string.poi_list));

        searchPoisButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (isPermissionsGranted()) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_DISTANCE_CHANGE,
                            getLocationListener());
                    onLocationChanged(
                            locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
                }
            }
        });

        if (showOnMapButton != null) {
            showOnMapButton.setEnabled(isButtonsEnabled);
            showOnMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getPoiListFragment().getSelectedPosition() != INVALID_POSITION) {
                        startActivityForResult(createLocationIntent(), REQUEST_CODE);
                    }
                }
            });
        }
    }

    @Override
    protected Intent createIntentForForward() {
        Intent intent = new Intent(getContext(), SelectArticleActivity.class);
        int selectedPosition = getListFragment().getSelectedPosition();
        if (getListElements() != null && selectedPosition != INVALID_POSITION) {
            PoiResponseResult selectedResult = getPois().get(selectedPosition);
            intent.putExtra(NAME_OF_SELECTED_POI, selectedResult.getName());
        }
        return intent;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        radius = progress * PROGRESS_SCALE;
        radiusView.setText(String.valueOf(radius));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //NOP
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //NOP
    }

    @Override
    public void onLocationChanged(Location location) {
        RestApiParams params = createSearchPoiParams(location.getLatitude(),
                location.getLongitude());
        new PoiHttpRequestTask(this).execute(params);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                isButtonsEnabled = true;
            }
        }
    }

    public void setAirQuality(int pollutionLevel) {
        TextView airQualityText = (TextView) findViewById(R.id.air_quality_text);
        RatingBar airQualityRatingBar = (RatingBar) findViewById(R.id.air_quality_ratingBar);
        if (airQualityText != null) {
            if (pollutionLevel <= LOW_POLLUTION_LEVEL) {
                airQualityText.setText(AIR_QUALITY_GOOD);
            } else if (pollutionLevel <= MEDIUM_POLLUTION_LEVEL) {
                airQualityText.setText(AIR_QUALITY_MEDIUM);
            } else {
                airQualityText.setText(AIR_QUALITY_BAD);
            }
        }

        if (airQualityRatingBar != null) {
            airQualityRatingBar.setRating(MAX_STARS - pollutionLevel);
        }

    }

    public void setPois(List<PoiResponseResult> pois) {
        setListElements(pois);
    }

    public List<PoiResponseResult> getPois() {
        return (List<PoiResponseResult>) getListElements();
    }

    public DefaultListFragment getPoiListFragment() {
        return getListFragment();
    }

    public void enableShowMapAndForwardButtons() {
        isButtonsEnabled = true;
        getForwardButton().setEnabled(true);
        showOnMapButton.setEnabled(true);
    }

    public void disableShowMapAndForwardButtons() {
        showOnMapButton.setEnabled(false);
        getForwardButton().setEnabled(false);
        showOnMapButton.setEnabled(false);
    }

    @Nullable
    private Intent createLocationIntent() {
        Intent intent = new Intent(getContext(), MapsActivity.class);
        int selectedPosition = getPoiListFragment().getSelectedPosition();
        if (selectedPosition != INVALID_POSITION && getListElements() != null) {
            intent.putExtra(Constants.Location.LATITUDE,
                    String.valueOf(getPois().get(selectedPosition)
                            .getGeometry().getLocation().getLat()));
            intent.putExtra(Constants.Location.LONGITUDE,
                    String.valueOf(getPois().get(selectedPosition).
                            getGeometry().getLocation().getLng()));
            return intent;
        }
        return null;
    }

    private RestApiParams createSearchPoiParams(double latitude, double longitude) {
        RestApiParams params = new RestApiParams();
        params.setUri(NEARBY_SEARCH_API_URI);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("location", latitude + "," + longitude);
        queryParams.put("radius", String.valueOf(radius));
        queryParams.put("type", SEARCH_TYPES);
        queryParams.put("language", "pl");
        queryParams.put("key", API_KEY_GOOGLE);
        params.setQueryParams(queryParams);

        return params;
    }

    private void getUserPermissions() {

        if (!isPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS,
                    REQUEST_READ_PHONE_PERMISSIONS);
        }
    }

    private boolean isPermissionsGranted() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private LocationListener getLocationListener() {
        return this;
    }

    @Override
    public void onSelectItem(int selectedPosition) {
        if (selectedPosition != INVALID_POSITION && getListElements() != null) {
            PoiResponseResult selectedPoi = getPois().get(selectedPosition);
            new AirQualityRequestTask(getContext())
                    .execute(createGetAirQualityParams(selectedPoi.getGeometry().getLocation()));
        }
    }

    private RestApiParams createGetAirQualityParams(PoiLocation location) {
        RestApiParams params = new RestApiParams();
        params.setUri(AIRLY_API_URI);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("latitude", String.valueOf(location.getLat()));
        queryParams.put("longitude", String.valueOf(location.getLng()));
        queryParams.put("maxDistance", String.valueOf(SEARCH_AIR_MEASUREMENT_DISTANCE));
        params.setQueryParams(queryParams);

        return params;
    }
}

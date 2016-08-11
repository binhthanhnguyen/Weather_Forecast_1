package com.framgia.edu.weatherforecast.ui.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.edu.weatherforecast.R;
import com.framgia.edu.weatherforecast.data.daos.CityDAO;
import com.framgia.edu.weatherforecast.data.models.City;
import com.framgia.edu.weatherforecast.data.models.Constants;
import com.framgia.edu.weatherforecast.data.models.DataBlock;
import com.framgia.edu.weatherforecast.data.models.DataPoint;
import com.framgia.edu.weatherforecast.data.models.ForecastRequest;
import com.framgia.edu.weatherforecast.data.models.ForecastResponse;
import com.framgia.edu.weatherforecast.data.models.SpeedUnit;
import com.framgia.edu.weatherforecast.data.models.TemperatureUnit;
import com.framgia.edu.weatherforecast.service.FetchAddressIntentService;
import com.framgia.edu.weatherforecast.service.ForecastService;
import com.framgia.edu.weatherforecast.service.ServiceGenerator;
import com.framgia.edu.weatherforecast.ui.adapters.DailyAdapter;
import com.framgia.edu.weatherforecast.ui.adapters.HourlyAdapter;
import com.framgia.edu.weatherforecast.util.NetworkUtil;
import com.framgia.edu.weatherforecast.util.ResourceUtil;
import com.framgia.edu.weatherforecast.util.SettingsPreferences;
import com.framgia.edu.weatherforecast.util.Temperature;
import com.framgia.edu.weatherforecast.util.UnitConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_SETTINGS = 2;
    protected static final int REQUEST_CODE_CHECK_SETTINGS = 3;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 4;

    private CoordinatorLayout mCoordinatorLayout;
    private LinearLayout mLinearLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private CityDAO mCityDAO;
    private List<City> mCities;
    private City mCurrentCity;
    private ForecastService mForecastService;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private AddressResultReceiver mAddressResultReceiver;
    private LocationSettingsRequest mLocationSettingsRequest;
    private SimpleDateFormat mDateFormatter;
    private SimpleDateFormat mHourlyFormatter;
    private DecimalFormat mDecimalFormat;
    private ForecastResponse mForecastResponse;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerViewHourly;
    private RecyclerView mRecyclerViewDaily;
    private HourlyAdapter mHourlyAdapter;
    private DailyAdapter mDailyAdapter;
    private TextView mTextToday;
    private TextView mTextTemperature;
    private TextView mTextToolbarTitle;
    private TextView mTextSummary;
    private TextView mTextSunrise;
    private TextView mTextSunset;
    private TextView mTextTodaySummary;
    private TextView mTextHumidity;
    private TextView mTextWindSpeed;
    private TextView mTextPrecipitation;
    private TextView mTextPressure;
    private TextView mTextVisibility;
    private TextView mTextOzone;
    private Place mPlace;
    private Location mLocation;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mForecastService = ServiceGenerator.createService(ForecastService.class, Constants.API_KEY);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        mCityDAO = CityDAO.getInstance(this);

        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
            mAddressResultReceiver = new AddressResultReceiver(new Handler());
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        setupSwipeRefreshLayout(mSwipeRefreshLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mMenu = navigationView.getMenu();
        setupLocationMenu(mMenu);

        mDateFormatter = new SimpleDateFormat("EEEE");
        mHourlyFormatter = new SimpleDateFormat("hh:mm aa");
        mDecimalFormat = new DecimalFormat();

        mRecyclerViewHourly = (RecyclerView) findViewById(R.id.recycler_view_hourly);
        mRecyclerViewHourly
                .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mRecyclerViewDaily = (RecyclerView) findViewById(R.id.recycler_view_daily);
        mRecyclerViewDaily.setLayoutManager(new LinearLayoutManager(this));

        mTextTemperature = (TextView) findViewById(R.id.text_temperature);
        mTextSummary = (TextView) findViewById(R.id.text_summary);
        mTextToday = (TextView) findViewById(R.id.text_today);
        mTextToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mTextSunrise = (TextView) findViewById(R.id.text_sunrise);
        mTextSunset = (TextView) findViewById(R.id.text_sunset);
        mTextTodaySummary = (TextView) findViewById(R.id.text_today_summary);
        mTextHumidity = (TextView) findViewById(R.id.text_humidity);
        mTextWindSpeed = (TextView) findViewById(R.id.text_wind_speed);
        mTextPrecipitation = (TextView) findViewById(R.id.text_precipitation);
        mTextPressure = (TextView) findViewById(R.id.text_pressure);
        mTextVisibility = (TextView) findViewById(R.id.text_visibility);
        mTextOzone = (TextView) findViewById(R.id.text_ozone);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            try {
                AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().
                        setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .setFilter(autocompleteFilter)
                        .build(this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            } catch (GooglePlayServicesRepairableException e) {
                GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0).show();
            } catch (GooglePlayServicesNotAvailableException e) {
                String message = getString(R.string.error_message_google_availability) +
                        GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
                Log.e(TAG, message);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (item.getItemId() == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
        }

        for (City city : mCities) {
            if (item.getItemId() == city.getId()) {
                mPlace = null;
                mCurrentCity = city;
                LatLng latLng = new LatLng(city.getLatitude(), city.getLongitude());
                mProgressBar.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.GONE);
                fetchForecastAsync(latLng);
                mTextToolbarTitle.setText(city.getName());
                updateUi();
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                mPlace = PlaceAutocomplete.getPlace(this, data);
                String cityName = (String) mPlace.getName();
                mTextToolbarTitle.setText(cityName);
                LatLng latLng = mPlace.getLatLng();
                mProgressBar.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.GONE);
                fetchForecastAsync(latLng);
                if (mCityDAO.insert(new City(cityName, latLng.latitude, latLng.longitude))) {
                    mCities = mCityDAO.getAllCities();
                    City lastCity = mCities.get(mCities.size() -1);
                    addLocationMenuItem(mMenu, lastCity);
                } else {
                    Toast.makeText(this, R.string.error_message_can_not_add_location, Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Log.e(TAG, getString(R.string.error_place_autocomplete_result) +
                        PlaceAutocomplete.getStatus(this, data).toString());
            }

        } else if (requestCode == REQUEST_CODE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                if (mForecastResponse == null) return;
                int prevTemperatureUnit = data.getIntExtra(SettingsActivity.EXTRA_PREV_TEMPERATURE_UNIT, 0);
                int prevSpeedUnit = data.getIntExtra(SettingsActivity.EXTRA_PREV_WIND_SPEED_UNIT, 0);
                setUnits(prevTemperatureUnit, prevSpeedUnit);
            }
        } else if (requestCode == REQUEST_CODE_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "User agreed to make required location settings changes.");
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {

                            }
                        });

            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "User chose not to make required location settings changes.");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeLocationSettings();
            } else {
                Snackbar.make(mCoordinatorLayout, R.string.permission_not_granted, Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                changeLocationSettings();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLocation != null) {
                    if(!NetworkUtil.isConnected(this)) {
                        showNetworkSettingsDialog();
                        return;
                    }
                    FetchAddressIntentService.startIntentService(this, mAddressResultReceiver, mLocation);
                    new FetchForecastTask(this, mLocation.getLatitude(), mLocation.getLongitude()).execute();
                }

                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    status.startResolutionForResult(MainActivity.this, REQUEST_CODE_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (mLocation != null) {
            if(!NetworkUtil.isConnected(this)) {
                showNetworkSettingsDialog();
                return;
            }
            FetchAddressIntentService.startIntentService(this, mAddressResultReceiver, mLocation);
            new FetchForecastTask(this, mLocation.getLatitude(), mLocation.getLongitude()).execute();
        }
    }

    private void setupSwipeRefreshLayout(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isConnected(MainActivity.this)) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                if (mPlace != null) {
                    fetchForecastAsync(mPlace.getLatLng());
                    return;
                }

                if (mCurrentCity != null) {
                    fetchForecastAsync(new LatLng(mCurrentCity.getLatitude(), mCurrentCity.getLongitude()));
                    return;
                }

                if (mLocation != null) {
                    FetchAddressIntentService
                            .startIntentService(MainActivity.this, mAddressResultReceiver, mLocation);
                    fetchForecastAsync(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setupLocationMenu(Menu menu) {
        mCities = mCityDAO.getAllCities();
        for (City city : mCities) {
            menu.add(R.id.location_group, city.getId(), 0, city.getName())
                    .setIcon(R.drawable.ic_location_on);
        }
    }

    private void addLocationMenuItem(Menu menu, City city) {
        menu.add(R.id.location_group, city.getId(), 0, city.getName())
                .setIcon(R.drawable.ic_location_on);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void changeLocationSettings() {
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(mCoordinatorLayout, R.string.permission_location_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.button_label_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_LOCATION_PERMISSION);
                        }
                    })
                    .show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    private void fetchForecastAsync(LatLng latLng) {
        ForecastRequest request = new ForecastRequest();
        request.setLatitude(String.valueOf(latLng.latitude));
        request.setLongitude(String.valueOf(latLng.longitude));
        int temperatureUnit = SettingsPreferences.getStoredSettingsTemperature(this);
        if (temperatureUnit == TemperatureUnit.FAHRENHEIT) {
            request.setUnits(ForecastRequest.Units.US);
            SettingsPreferences.setStoredSettingsWindSpeed(MainActivity.this, SpeedUnit.MILES_PER_HOUR);
        } else {
            int speedUnit = SettingsPreferences.getStoredSettingsWindSpeed(this);
            if (speedUnit == SpeedUnit.METRES_PER_SECOND) {
                request.setUnits(ForecastRequest.Units.SI);
            } else if (speedUnit == SpeedUnit.KILOMETRES_PER_HOUR) {
                request.setUnits(ForecastRequest.Units.CA);
            } else {
                request.setUnits(ForecastRequest.Units.UK);
            }
        }
        request.setLanguage(ForecastRequest.Language.ENGLISH);
        Call<ForecastResponse> responseCall = mForecastService.getForecast(request, request.getQueryParams());
        responseCall.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful()) {
                    mForecastResponse = response.body();
                    mProgressBar.setVisibility(View.GONE);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    updateUi();
                }
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                mProgressBar.setVisibility(View.GONE);
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setUnits(int prevTemperatureUnit, int prevSpeedUnit) {
        int currentTemperatureUnit = SettingsPreferences.getStoredSettingsTemperature(this);
        if (currentTemperatureUnit != prevTemperatureUnit) {
            if (currentTemperatureUnit == TemperatureUnit.CELSIUS) {
                mForecastResponse = UnitConverter.temperatureToCelsius(mForecastResponse);
            } else {
                mForecastResponse = UnitConverter.temperatureToFahrenheit(mForecastResponse);
            }
        }

        int currentSpeedUnit = SettingsPreferences.getStoredSettingsWindSpeed(this);
        if (currentSpeedUnit != prevSpeedUnit) {
            switch (currentSpeedUnit) {
                case SpeedUnit.KILOMETRES_PER_HOUR :
                    if (prevSpeedUnit == SpeedUnit.MILES_PER_HOUR) {
                        mForecastResponse = UnitConverter.speedFromMphToKph(mForecastResponse);
                    } else {
                        mForecastResponse = UnitConverter.speedFromMetresPerSecondToKph(mForecastResponse);
                    }
                    break;
                case SpeedUnit.METRES_PER_SECOND :
                    if (prevSpeedUnit == SpeedUnit.KILOMETRES_PER_HOUR) {
                        mForecastResponse = UnitConverter.speedFromKphToMetresPerSecond(mForecastResponse);
                    } else {
                        mForecastResponse = UnitConverter.speedFromMphToMetresPerSecond(mForecastResponse);
                    }
                    break;
                case SpeedUnit.MILES_PER_HOUR :
                    if (prevSpeedUnit == SpeedUnit.KILOMETRES_PER_HOUR) {
                        mForecastResponse = UnitConverter.speedFromKphToMph(mForecastResponse);
                    } else {
                        mForecastResponse = UnitConverter.speedFromMetresPerSecondToMph(mForecastResponse);
                    }
                    break;
            }
        }
        updateUi();
    }

    private void updateUi() {
        if (mForecastResponse != null) {
            DataPoint currentDataPoint = mForecastResponse.getCurrently();
            DataBlock dailyDataBlock = mForecastResponse.getDaily();
            DataBlock hourlyDataBlock = mForecastResponse.getHourly();
            String timeZone = mForecastResponse.getTimezone();

            mCoordinatorLayout.setBackgroundResource(ResourceUtil
                    .getBackgroundIdentifier(currentDataPoint.getIcon()));

            mTextSummary.setText(currentDataPoint.getSummary());
            mTextTemperature.setText(Temperature.stringForTemperature(currentDataPoint.getTemperature()));

            mDateFormatter.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date dateTime = new Date(currentDataPoint.getTime() * 1000L);
            mTextToday.setText(mDateFormatter.format(dateTime));

            if (mDailyAdapter == null) {
                mDailyAdapter = new DailyAdapter(this, dailyDataBlock, timeZone);
                mRecyclerViewDaily.setAdapter(mDailyAdapter);
            } else {
                mDailyAdapter.setDataBlock(dailyDataBlock);
                mDailyAdapter.setTimeZone(timeZone);
                mDailyAdapter.notifyDataSetChanged();
            }

            if (mHourlyAdapter == null) {
                mHourlyAdapter = new HourlyAdapter(this, hourlyDataBlock, timeZone);
                mRecyclerViewHourly.setAdapter(mHourlyAdapter);
            } else {
                mHourlyAdapter.setDataBlock(hourlyDataBlock);
                mHourlyAdapter.setTimeZone(timeZone);
                mHourlyAdapter.notifyDataSetChanged();
            }

            DataPoint todayDataPoint = dailyDataBlock.getData().get(0);
            mHourlyFormatter.setTimeZone(TimeZone.getTimeZone(timeZone));

            mTextTodaySummary.setText(String.format(getString(R.string.label_today)
                    + ": " + todayDataPoint.getSummary()));
            mTextSunrise.setText(mHourlyFormatter.format(new Date(todayDataPoint.getSunriseTime() * 1000L)));
            mTextSunset.setText(mHourlyFormatter.format(new Date(todayDataPoint.getSunsetTime() * 1000L)));
            mTextHumidity.setText(Math.round(currentDataPoint.getHumidity() * 100)
                    + getString(R.string.unit_label_percentage));
            String speedUnitLabel = null;
            switch (SettingsPreferences.getStoredSettingsWindSpeed(this)) {
                case SpeedUnit.KILOMETRES_PER_HOUR :
                    speedUnitLabel = getString(R.string.label_kilometres_per_hour);
                    break;
                case SpeedUnit.METRES_PER_SECOND :
                    speedUnitLabel = getString(R.string.label_metres_per_second);
                    break;
                case SpeedUnit.MILES_PER_HOUR :
                    speedUnitLabel = getString(R.string.label_miles_per_hour);
                    break;
                default:
                    speedUnitLabel = getString(R.string.label_miles_per_hour);
                    break;
            }

            mDecimalFormat.applyPattern(".# " + speedUnitLabel);
            mTextWindSpeed.setText(mDecimalFormat.format(currentDataPoint.getWindSpeed()));
            mTextPrecipitation.setText(currentDataPoint.getPrecipIntensity());
            mTextPressure.setText(String.valueOf(currentDataPoint.getPressure()));
            mTextVisibility.setText(String.valueOf(currentDataPoint.getVisibility()));
            mTextOzone.setText(String.valueOf(Math.round(currentDataPoint.getOzone() / 25)));
        }
    }

    private void showNetworkSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle(R.string.title_cellular_data_is_turned_off)
                .setMessage(R.string.message_turn_on_cellular_data)
                .setPositiveButton(R.string.button_label_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.button_label_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private static class FetchForecastTask extends AsyncTask<Void, Void, ForecastResponse> {
        private ForecastResponse forecastResponse;
        private WeakReference<MainActivity> mWeakReference;
        private MainActivity mMainActivity;
        private double mLatitude;
        private double mLongitude;

        public FetchForecastTask(MainActivity activity, double latitude, double longitude) {
            mWeakReference = new WeakReference<MainActivity>(activity);
            mMainActivity = mWeakReference.get();
            mLatitude = latitude;
            mLongitude = longitude;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mMainActivity != null) {
                mMainActivity.mProgressBar.setVisibility(View.VISIBLE);
                mMainActivity.mLinearLayout.setVisibility(View.GONE);
            }
        }

        @Override
        protected ForecastResponse doInBackground(Void... params) {
            if (mMainActivity != null) {
                ForecastRequest request = new ForecastRequest();
                request.setLatitude(String.valueOf(mLatitude));
                request.setLongitude(String.valueOf(mLongitude));

                int temperatureUnit = SettingsPreferences.getStoredSettingsTemperature(mMainActivity);
                int speedUnit = SettingsPreferences.getStoredSettingsWindSpeed(mMainActivity);
                if (temperatureUnit == TemperatureUnit.FAHRENHEIT) {
                    request.setUnits(ForecastRequest.Units.US);
                    SettingsPreferences.setStoredSettingsWindSpeed(mMainActivity, SpeedUnit.MILES_PER_HOUR);
                } else {
                    if (speedUnit == SpeedUnit.METRES_PER_SECOND) {
                        request.setUnits(ForecastRequest.Units.SI);
                    } else if (speedUnit == SpeedUnit.KILOMETRES_PER_HOUR) {
                        request.setUnits(ForecastRequest.Units.CA);
                    } else {
                        request.setUnits(ForecastRequest.Units.UK);
                    }
                }
                request.setLanguage(ForecastRequest.Language.ENGLISH);
                try {
                    Call<ForecastResponse> responseCall = mMainActivity.mForecastService
                            .getForecast(request, request.getQueryParams());
                    forecastResponse = responseCall.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return forecastResponse;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ForecastResponse forecastResponse) {
            if (mMainActivity != null) {
                mMainActivity.mForecastResponse = forecastResponse;
                mMainActivity.mProgressBar.setVisibility(View.GONE);
                mMainActivity.mLinearLayout.setVisibility(View.VISIBLE);
                mMainActivity.updateUi();
            }
        }
    }

    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == Constants.RESULT_OK) {
                String address = resultData.getString(Constants.BUNDLE_ADDRESS);
                mTextToolbarTitle.setText(address);
            } else if(resultCode == Constants.RESULT_FAILURE) {
                mTextToolbarTitle.setText(R.string.title_no_city_found);
            }
        }
    }


}

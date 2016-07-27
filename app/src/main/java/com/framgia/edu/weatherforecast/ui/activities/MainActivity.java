package com.framgia.edu.weatherforecast.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.framgia.edu.weatherforecast.R;
import com.framgia.edu.weatherforecast.data.models.Constant;
import com.framgia.edu.weatherforecast.data.models.DataBlock;
import com.framgia.edu.weatherforecast.data.models.DataPoint;
import com.framgia.edu.weatherforecast.data.models.ForecastRequest;
import com.framgia.edu.weatherforecast.data.models.ForecastResponse;
import com.framgia.edu.weatherforecast.data.models.SpeedUnit;
import com.framgia.edu.weatherforecast.data.models.TemperatureUnit;
import com.framgia.edu.weatherforecast.service.ForecastService;
import com.framgia.edu.weatherforecast.service.ServiceGenerator;
import com.framgia.edu.weatherforecast.ui.adapters.DailyAdapter;
import com.framgia.edu.weatherforecast.ui.adapters.HourlyAdapter;
import com.framgia.edu.weatherforecast.util.SettingsPreferences;
import com.framgia.edu.weatherforecast.util.Temperature;
import com.framgia.edu.weatherforecast.util.UnitConverter;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_SETTINGS = 2;
    private ForecastService mForecastService;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mForecastService = ServiceGenerator.createService(ForecastService.class, Constant.API_KEY);
        //TODO Test
        new FetchForecastTask(this, 21.0278, 105.8342).execute();

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

        mDateFormatter = new SimpleDateFormat("EEEE");
        mHourlyFormatter = new SimpleDateFormat("hh:mm aa");
        mDecimalFormat = new DecimalFormat();

        mDateFormatter = new SimpleDateFormat("EEEE");
        mHourlyFormatter = new SimpleDateFormat("hh:mm aa");

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
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                mPlace = PlaceAutocomplete.getPlace(this, data);
                mTextToolbarTitle.setText(mPlace.getName());
                LatLng latLng = mPlace.getLatLng();
                fetchForecastAsync(latLng);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Log.e(TAG, getString(R.string.error_place_autocomplete_result) +
                        PlaceAutocomplete.getStatus(this, data).toString());
            }

        } else if (requestCode == REQUEST_CODE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                int prevTemperatureUnit = data.getIntExtra(SettingsActivity.EXTRA_PREV_TEMPERATURE_UNIT, 0);
                int prevSpeedUnit = data.getIntExtra(SettingsActivity.EXTRA_PREV_WIND_SPEED_UNIT, 0);
                setUnits(prevTemperatureUnit, prevSpeedUnit);
            }
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
                    updateUi();
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
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

            mTextTodaySummary.setText(getString(R.string.label_today_colon) + todayDataPoint.getSummary());
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
                mMainActivity.updateUi();
            }
        }
    }
}

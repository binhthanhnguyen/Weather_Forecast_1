package com.framgia.edu.weatherforecast.ui.activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.framgia.edu.weatherforecast.R;
import com.framgia.edu.weatherforecast.data.models.Constant;
import com.framgia.edu.weatherforecast.data.models.DataPoint;
import com.framgia.edu.weatherforecast.data.models.ForecastRequest;
import com.framgia.edu.weatherforecast.data.models.ForecastResponse;
import com.framgia.edu.weatherforecast.service.ForecastService;
import com.framgia.edu.weatherforecast.service.ServiceGenerator;
import com.framgia.edu.weatherforecast.ui.adapters.DailyAdapter;
import com.framgia.edu.weatherforecast.ui.adapters.HourlyAdapter;
import com.framgia.edu.weatherforecast.util.Temperature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private ForecastResponse mForecastResponse;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerViewHourly;
    private RecyclerView mRecyclerViewDaily;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        new FetchForecastTask().execute();

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

        mRecyclerViewHourly = (RecyclerView) findViewById(R.id.recycler_view_hourly);
        mRecyclerViewDaily = (RecyclerView) findViewById(R.id.recycler_view_daily);

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
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class FetchForecastTask extends AsyncTask<Void, Void, ForecastResponse> {

        ForecastResponse forecastResponse;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ForecastResponse doInBackground(Void... params) {
            ForecastService forecastService = ServiceGenerator.createService(ForecastService.class, Constant.API_KEY);
            ForecastRequest request = new ForecastRequest();
            //TODO Test
            request.setLatitude("21.027764");
            request.setLongitude("105.834160");
            request.setUnits(ForecastRequest.Units.SI);
            request.setLanguage(ForecastRequest.Language.ENGLISH);
            try {
                Call<ForecastResponse> responseCall = forecastService.getForecast(request, request.getQueryParams());
                forecastResponse = responseCall.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return forecastResponse;
        }

        @Override
        protected void onPostExecute(ForecastResponse forecastResponse) {
            mForecastResponse = forecastResponse;
            mProgressBar.setVisibility(View.GONE);
            setupView();
        }
    }

    private void setupView() {
        mTextSummary.setText(mForecastResponse.getCurrently().getSummary());
        mTextTemperature.setText(Temperature.stringForTemperature(mForecastResponse.getCurrently().getTemperature()));
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mForecastResponse.getTimezone()));
        Date dateTime = new Date(mForecastResponse.getCurrently().getTime() * 1000L);
        mTextToday.setText(formatter.format(dateTime));

        mRecyclerViewHourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewHourly.setAdapter(new HourlyAdapter(this, mForecastResponse.getHourly(), mForecastResponse.getTimezone()));

        mRecyclerViewDaily.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewDaily.setAdapter(new DailyAdapter(this, mForecastResponse.getDaily(), mForecastResponse.getTimezone()));

        DataPoint todayData = mForecastResponse.getDaily().getData().get(0);
        SimpleDateFormat hourlyFormatter = new SimpleDateFormat("hh:mm aa");
        hourlyFormatter.setTimeZone(TimeZone.getTimeZone(mForecastResponse.getTimezone()));

        mTextTodaySummary.setText(getString(R.string.label_today_colon) + todayData.getSummary());
        mTextSunrise.setText(hourlyFormatter.format(new Date(todayData.getSunriseTime() * 1000L)));
        mTextSunset.setText(hourlyFormatter.format(new Date(todayData.getSunsetTime() * 1000L)));
        mTextHumidity.setText(todayData.getHumidity() * 100 + getString(R.string.unit_label_percentage));
        mTextWindSpeed.setText(String.valueOf(todayData.getWindSpeed()));
        mTextPrecipitation.setText(todayData.getPrecipIntensity());
        mTextPressure.setText(String.valueOf(todayData.getPressure()));
        mTextVisibility.setText(String.valueOf(mForecastResponse.getCurrently().getVisibility()));
        mTextOzone.setText(String.valueOf(Math.round(mForecastResponse.getCurrently().getOzone() / 25)));
    }
}

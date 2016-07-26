package com.framgia.edu.weatherforecast.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.framgia.edu.weatherforecast.R;
import com.framgia.edu.weatherforecast.data.models.TemperatureUnit;
import com.framgia.edu.weatherforecast.util.SettingsPreferences;

/**
 * Created by binh on 7/22/16.
 */
public class SettingsActivity extends AppCompatActivity {
    public static final String EXTRA_PREV_TEMPERATURE_UNIT = "EXTRA_PREV_TEMPERATURE_UNIT";
    private static final String TAG = "SettingActivity";

    private RadioGroup mRadioGroupTemperature;
    private RadioGroup mRadioGroupWindSpeed;
    private RadioButton mRadioButtonCelsius;
    private RadioButton mRadioButtonFahrenheit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.title_settings);

        mRadioGroupTemperature = (RadioGroup) findViewById(R.id.radio_temperature_unit);
        mRadioButtonCelsius = (RadioButton) findViewById(R.id.radio_celsius);
        mRadioButtonFahrenheit = (RadioButton) findViewById(R.id.radio_fahrenheit);
        if (SettingsPreferences.getStoredSettingsTemperature(this) == TemperatureUnit.CELSIUS) {
            mRadioButtonCelsius.setChecked(true);
        } else {
            mRadioButtonFahrenheit.setChecked(true);
        }

        mRadioGroupWindSpeed = (RadioGroup) findViewById(R.id.radio_wind_speed_unit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int prevTemperatureUnit = SettingsPreferences.getStoredSettingsTemperature(this);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PREV_TEMPERATURE_UNIT, prevTemperatureUnit);
        if (mRadioGroupTemperature.getCheckedRadioButtonId() == R.id.radio_celsius &&
                prevTemperatureUnit != TemperatureUnit.CELSIUS) {
            SettingsPreferences.setStoredSettingsTemperature(this, TemperatureUnit.CELSIUS);
            setResult(RESULT_OK, intent);
        } else if(mRadioGroupTemperature.getCheckedRadioButtonId() == R.id.radio_fahrenheit &&
                prevTemperatureUnit != TemperatureUnit.FAHRENHEIT) {
            SettingsPreferences.setStoredSettingsTemperature(this, TemperatureUnit.FAHRENHEIT);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
    }
}

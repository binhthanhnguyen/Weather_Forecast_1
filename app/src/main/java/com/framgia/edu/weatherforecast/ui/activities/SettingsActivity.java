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
import com.framgia.edu.weatherforecast.data.models.SpeedUnit;
import com.framgia.edu.weatherforecast.data.models.TemperatureUnit;
import com.framgia.edu.weatherforecast.util.SettingsPreferences;

/**
 * Created by binh on 7/22/16.
 */
public class SettingsActivity extends AppCompatActivity {
    public static final String EXTRA_PREV_TEMPERATURE_UNIT = "EXTRA_PREV_TEMPERATURE_UNIT";
    public static final String EXTRA_PREV_WIND_SPEED_UNIT = "EXTRA_PREV_WIND_SPEED_UNIT";
    private static final String TAG = "SettingActivity";
    private RadioGroup mRadioGroupTemperature;
    private RadioGroup mRadioGroupWindSpeed;
    private RadioButton mRadioButtonCelsius;
    private RadioButton mRadioButtonFahrenheit;
    private RadioButton mRadioButtonMphUnit;
    private RadioButton mRadioButtonKphUnit;
    private RadioButton mRadioButtonMpsUnit;

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
        mRadioButtonKphUnit = (RadioButton) findViewById(R.id.radio_kilometres_per_hour);
        mRadioButtonMphUnit = (RadioButton) findViewById(R.id.radio_miles_per_hour);
        mRadioButtonMpsUnit = (RadioButton) findViewById(R.id.radio_metres_per_second);

        int windSpeedUnit = SettingsPreferences.getStoredSettingsWindSpeed(this);
        if (windSpeedUnit == SpeedUnit.KILOMETRES_PER_HOUR) {
            mRadioButtonKphUnit.setChecked(true);
        } else if (windSpeedUnit == SpeedUnit.METRES_PER_SECOND) {
            mRadioButtonMpsUnit.setChecked(true);
        } else {
            mRadioButtonMphUnit.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int prevTemperatureUnit = SettingsPreferences.getStoredSettingsTemperature(this);

        if (mRadioGroupTemperature.getCheckedRadioButtonId() == R.id.radio_celsius &&
                prevTemperatureUnit != TemperatureUnit.CELSIUS) {
            SettingsPreferences.setStoredSettingsTemperature(this, TemperatureUnit.CELSIUS);
        } else if(mRadioGroupTemperature.getCheckedRadioButtonId() == R.id.radio_fahrenheit &&
                prevTemperatureUnit != TemperatureUnit.FAHRENHEIT) {
            SettingsPreferences.setStoredSettingsTemperature(this, TemperatureUnit.FAHRENHEIT);
        }

        int prevSpeedUnit = SettingsPreferences.getStoredSettingsWindSpeed(this);
        switch (mRadioGroupWindSpeed.getCheckedRadioButtonId()) {
            case R.id.radio_kilometres_per_hour :
                if (prevSpeedUnit != SpeedUnit.KILOMETRES_PER_HOUR)
                    SettingsPreferences.setStoredSettingsWindSpeed(this, SpeedUnit.KILOMETRES_PER_HOUR);
                break;
            case R.id.radio_metres_per_second :
                if (prevSpeedUnit != SpeedUnit.METRES_PER_SECOND)
                    SettingsPreferences.setStoredSettingsWindSpeed(this, SpeedUnit.METRES_PER_SECOND);
                break;
            case R.id.radio_miles_per_hour :
                if (prevSpeedUnit != SpeedUnit.MILES_PER_HOUR)
                    SettingsPreferences.setStoredSettingsWindSpeed(this, SpeedUnit.MILES_PER_HOUR);
                break;
            default: SettingsPreferences.setStoredSettingsWindSpeed(this, SpeedUnit.MILES_PER_HOUR);
        }

        int currentTemperatureUnit = SettingsPreferences.getStoredSettingsTemperature(this);
        int currentSpeedUnit = SettingsPreferences.getStoredSettingsWindSpeed(this);
        if (currentTemperatureUnit != prevTemperatureUnit || currentSpeedUnit != prevSpeedUnit) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PREV_TEMPERATURE_UNIT, prevTemperatureUnit);
            intent.putExtra(EXTRA_PREV_WIND_SPEED_UNIT, prevSpeedUnit);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
    }
}

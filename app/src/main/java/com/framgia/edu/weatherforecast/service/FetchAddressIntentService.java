package com.framgia.edu.weatherforecast.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.framgia.edu.weatherforecast.R;
import com.framgia.edu.weatherforecast.data.models.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by binh on 7/29/16.
 */
public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIntentService";
    private static final String EXTRA_RESULT_RECEIVER = "EXTRA_RESULT_RECEIVER";
    private static final String EXTRA_LOCATION = "EXTRA_LOCATION";

    private ResultReceiver mResultReceiver;
    private Location mLocation;

    public FetchAddressIntentService() {
        super(TAG);
    }

    public static void startIntentService(Context context, ResultReceiver resultReceiver, Location location) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        intent.putExtra(EXTRA_LOCATION, location);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mResultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        mLocation = intent.getParcelableExtra(EXTRA_LOCATION);

        if (mLocation == null) {
            String errorMessage = getString(R.string.error_message_no_location_data_provided);
            deliverResultToReceiver(Constants.RESULT_FAILURE, errorMessage);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
            if (addressList != null && !addressList.isEmpty()) {
                deliverResultToReceiver(Constants.RESULT_OK, addressList.get(0).getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deliverResultToReceiver(int resultCode, String address) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_ADDRESS, address);
        mResultReceiver.send(resultCode, bundle);
    }
}

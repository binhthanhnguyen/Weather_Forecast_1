package com.framgia.edu.weatherforecast.service;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        List<Address> addresses = null;

        if (mLocation == null) {
            String errorMessage = getString(R.string.error_message_no_location_data_provided);
            deliverResultToReceiver(Constants.RESULT_FAILURE, errorMessage);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                deliverResultToReceiver(Constants.RESULT_OK, addresses.get(0).getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses == null) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder =
                    HttpUrl.parse(Constants.GOOGLE_MAP_API_BASE_URL).newBuilder();
            urlBuilder.addQueryParameter(Constants.PARAM_LAT_LNG,
                    mLocation.getLatitude() + ", " + mLocation.getLongitude());
            urlBuilder.addQueryParameter(Constants.PARAM_RESULT_TYPE, Constants.ARGUMENT_LOCALITY);
            urlBuilder.addQueryParameter(Constants.PARAM_KEY, Constants.GOOGLE_MAP_API_KEY);
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder().url(url).build();
            String address = null;
            try {
                Response response = client.newCall(request).execute();
                address = parseAddress(response);
                if (address != null) {
                    deliverResultToReceiver(Constants.RESULT_OK, address);
                } else {
                    deliverResultToReceiver(Constants.RESULT_FAILURE,
                            getString(R.string.error_message_no_address_found));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String parseAddress(Response response) {
        try {
            JSONObject jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getString(Constants.RESPONSE_STATUS).equals(Constants.STATUS_OK)) {
                jsonObject = jsonObject.getJSONArray(Constants.RESPONSE_RESULTS).getJSONObject(0);
                jsonObject = jsonObject.getJSONArray(Constants.RESPONSE_ADDRESS_COMPONENTS).getJSONObject(0);
                return jsonObject.getString(Constants.RESPONSE_LONG_NAME);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deliverResultToReceiver(int resultCode, String address) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_ADDRESS, address);
        mResultReceiver.send(resultCode, bundle);
    }
}

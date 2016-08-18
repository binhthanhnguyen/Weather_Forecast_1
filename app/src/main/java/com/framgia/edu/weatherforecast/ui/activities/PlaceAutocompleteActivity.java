package com.framgia.edu.weatherforecast.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.framgia.edu.weatherforecast.R;
import com.framgia.edu.weatherforecast.ui.adapters.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

/**
 * Created by binh on 8/19/16.
 */
public class PlaceAutocompleteActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "PlaceAutocompleteActivity";
    public static final String BUNDLE_CITY_NAME = "EXTRA_CITY_NAME";
    public static final String BUNDLE_LATITUDE = "EXTRA_LATITUDE";
    public static final String BUNDLE_LONGITUDE = "EXTRA_LONGITUDE";


    private RecyclerView mRecyclerView;
    private GoogleApiClient mGoogleApiClient;
    private AutocompleteFilter mAutocompleteFilter;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_autocomplete);
        buildGoogleApiClient();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, null, mAutocompleteFilter);
        mRecyclerView.setAdapter(mPlaceAutocompleteAdapter);
        mPlaceAutocompleteAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = (MenuItem) menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint(getString(R.string.action_search));
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(mGoogleApiClient.isConnected()) {
                    mPlaceAutocompleteAdapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mGoogleApiClient.isConnected()) {
                    mPlaceAutocompleteAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    private PlaceAutocompleteAdapter.OnItemClickListener mOnItemClickListener =
            new PlaceAutocompleteAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(AutocompletePrediction prediction) {
                    if(mGoogleApiClient.isConnected()) {
                        PendingResult<PlaceBuffer> placeResult =
                                Places.GeoDataApi.getPlaceById(mGoogleApiClient, prediction.getPlaceId());
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {

                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(!places.getStatus().isSuccess()) {
                                    Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                                    places.release();
                                    return;
                                }

                                final Place place = places.get(0);
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString(BUNDLE_CITY_NAME, place.getName().toString());
                                bundle.putDouble(BUNDLE_LATITUDE, place.getLatLng().latitude);
                                bundle.putDouble(BUNDLE_LONGITUDE, place.getLatLng().longitude);
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                                finish();
                                places.release();
                            }
                        });
                    }
                }
            };

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }
}

package com.framgia.binhnguyen.weatherforecast.service;

import com.framgia.binhnguyen.weatherforecast.data.models.ForecastRequest;
import com.framgia.binhnguyen.weatherforecast.data.models.ForecastResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by binh on 7/14/16.
 */
public interface ForecastService {
    @GET("{request}")
    Call<ForecastResponse> getForecast(@Path("request") ForecastRequest forecastRequest, @QueryMap Map<String, String> query);

    @GET("{request}")
    Call<ForecastResponse> getForecast(@Path("request") ForecastRequest forecastRequest);
}

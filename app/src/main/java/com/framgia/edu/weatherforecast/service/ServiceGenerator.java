package com.framgia.edu.weatherforecast.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by binh on 7/15/16.
 */
public class ServiceGenerator {
    public static final String API_BASE_URL = "https://api.forecast.io/forecast/";

    public static <S> S createService(Class<S> serviceClass, String apiKey) {
        String apiUrl = API_BASE_URL + apiKey + "/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(serviceClass);
    }
}

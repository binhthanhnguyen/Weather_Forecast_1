package com.framgia.edu.weatherforecast.data.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by binh on 7/14/16.
 */
public class ForecastRequest {
    private static final String UNITS_KEY = "units";
    private static final String LANGUAGE_KEY = "lang";
    private String mLatitude;
    private String mLongitude;
    private String mTime;
    private Units mUnits;
    private Language mLanguage;


    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        this.mLongitude = longitude;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        this.mLatitude = latitude;
    }

    private Boolean useTime() {
        return mTime != null && !mTime.equals("");
    }

    public Units getUnits() {
        return mUnits;
    }

    public void setUnits(Units units) {
        mUnits = units;
    }

    public Language getLanguage() {
        return mLanguage;
    }

    public void setLanguage(Language language) {
        mLanguage = language;
    }

    public Map<String, String> getQueryParams() {
        Map<String, String> query = new HashMap<>();
        query.put(UNITS_KEY, mUnits.toString());
        query.put(LANGUAGE_KEY, mLanguage.toString());
        return query;
    }

    public enum Units {
        SI("si"),
        US("us"),
        CA("ca"),
        UK("uk"),
        AUTO("auto");
        private String mValue;
        Units(String value) {
            mValue = value;
        }
        @Override
        public String toString() {
            return mValue;
        }
    }

    public enum Language {
        ARABIC("ar"),
        BOSNIAN("bs"),
        GERMAN("de"),
        GREEK("el"),
        ENGLISH("en"),
        SPANISH("es"),
        FRENCH("fr"),
        CROATIAN("hr"),
        ITALIAN("it"),
        DUTCH("nl"),
        POLISH("pl"),
        PORTUGUESE("pt"),
        RUSSIAN("ru"),
        SLOVAK("sk"),
        SWEDISH("sv"),
        TETUM("tet"),
        TURKISH("tr"),
        UKRAINIAN("uk"),
        PIG_LATIN("x-pig-latin"),
        CHINESE("zh"),
        CHINESE_TRADITIONAL("zh-tw");
        private String mValue;
        private Language(String value) {
            mValue = value;
        }
        @Override
        public String toString() {
            return mValue;
        }
    }

    @Override
    public String toString() {
        String params = mLatitude + "," + mLongitude;
        return  useTime() ?  params + "," + mTime : params;
    }
}

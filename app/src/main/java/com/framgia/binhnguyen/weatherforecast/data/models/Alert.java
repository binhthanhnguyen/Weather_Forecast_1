package com.framgia.binhnguyen.weatherforecast.data.models;

/**
 * Created by binh on 7/6/16.
 */
public class Alert {
    private String mTitle;
    private long mExpires;
    private String mDescription;
    private String mUri;

    public String getTitle() {
        return mTitle;
    }

    public long getExpires() {
        return mExpires;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUri() {
        return mUri;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setExpires(long expires) {
        this.mExpires = expires;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setUri(String uri) {
        this.mUri = uri;
    }
}

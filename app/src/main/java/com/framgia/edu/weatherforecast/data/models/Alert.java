package com.framgia.edu.weatherforecast.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by binh on 7/6/16.
 */
public class Alert {
    @SerializedName("title")
    private String mTitle;
    @SerializedName("expires")
    private long mExpires;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("uri")
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

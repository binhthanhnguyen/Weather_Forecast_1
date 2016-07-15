package com.framgia.binhnguyen.weatherforecast.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by binh on 7/6/16.
 */
public class DataBlock {
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("data")
    private List<DataPoint> mData;

    public String getIcon() {
        return mIcon;
    }

    public String getSummary() {
        return mSummary;
    }

    public List<DataPoint> getData() {
        return mData;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public void setData(List<DataPoint> data) {
        this.mData = data;
    }
}

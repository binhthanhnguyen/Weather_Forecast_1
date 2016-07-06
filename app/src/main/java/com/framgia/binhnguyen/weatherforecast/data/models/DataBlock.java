package com.framgia.binhnguyen.weatherforecast.data.models;

import java.util.List;

/**
 * Created by binh on 7/6/16.
 */
public class DataBlock {
    private String mSummary;
    private String mIcon;
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

package com.framgia.edu.weatherforecast.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.edu.weatherforecast.R;
import com.framgia.edu.weatherforecast.data.models.DataBlock;
import com.framgia.edu.weatherforecast.data.models.DataPoint;
import com.framgia.edu.weatherforecast.util.ResourceUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by binh on 7/18/16.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyHolder> {
    private DataBlock mDataBlock;
    private String mTimeZone;

    private LayoutInflater mInflater;

    public DailyAdapter(Context context, DataBlock dataBlock, String timeZone) {
        mDataBlock = dataBlock;
        if (mDataBlock != null && mDataBlock.getData().size() > 7) mDataBlock.getData().remove(0);
        mTimeZone = timeZone;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DailyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.daily_item, parent, false);
        return new DailyHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyHolder holder, int position) {
        DataPoint dailyItem = mDataBlock.getData().get(position);
        holder.bindDailyItem(dailyItem);
    }

    @Override
    public int getItemCount() {
        return mDataBlock.getData().size();
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public void setDataBlock(DataBlock dataBlock) {
        mDataBlock = dataBlock;
        if (mDataBlock != null && mDataBlock.getData().size() > 7) mDataBlock.getData().remove(0);
    }

    public class DailyHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mTextDay;
        private ImageView mImageIcon;
        private TextView mTextTemperature;

        public DailyHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextDay = (TextView) itemView.findViewById(R.id.text_day);
            mImageIcon = (ImageView) itemView.findViewById(R.id.image_icon);
            mTextTemperature = (TextView) itemView.findViewById(R.id.text_temperature);
        }

        public void bindDailyItem(DataPoint dataPoint) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(mTimeZone));
            Date dateTime = new Date(dataPoint.getTime() * 1000L);
            mTextDay.setText(simpleDateFormat.format(dateTime));
            mImageIcon.setImageResource(ResourceUtil.getIdentifier(dataPoint.getIcon()));
            mTextTemperature.setText(String.valueOf(Math.round(dataPoint.getTemperatureMax())));
        }
    }
}

package com.framgia.edu.weatherforecast.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
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
public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyHolder> {
    private DataBlock mDataBlock;
    private String mTimeZone;
    private LayoutInflater mInflater;

    public HourlyAdapter(Context context, DataBlock dataBlock, String timeZone) {
        mDataBlock = dataBlock;
        mTimeZone = timeZone;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HourlyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.hourly_item, parent, false);
        return new HourlyHolder(view);
    }

    @Override
    public void onBindViewHolder(HourlyHolder holder, int position) {
        DataPoint dataPoint = mDataBlock.getData().get(position);
        holder.bindHourlyItem(dataPoint);
    }

    @Override
    public int getItemCount() {
        return mDataBlock.getData().size();
    }

    public void setDataBlock(DataBlock dataBlock) {
        mDataBlock = dataBlock;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public class HourlyHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mTextTime;
        private ImageView mImageIcon;
        private TextView mTextTemperature;

        public HourlyHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextTime = (TextView) itemView.findViewById(R.id.text_time);
            mImageIcon = (ImageView) itemView.findViewById(R.id.image_icon);
            mTextTemperature = (TextView) itemView.findViewById(R.id.text_temperature);
        }

        public void bindHourlyItem(DataPoint dataPoint) {
            SimpleDateFormat formatter = new SimpleDateFormat("hhaa");
            formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
            Date dateTime = new Date(dataPoint.getTime() * 1000L);
            if (dataPoint.getTime() == mDataBlock.getData().get(0).getTime()) {
                mTextTime.setText(R.string.label_now);
                mTextTime.setTypeface(null, Typeface.BOLD);
            } else {
                mTextTime.setText(formatter.format(dateTime));
                mTextTime.setTypeface(null, Typeface.NORMAL);
            }
            mImageIcon.setImageResource(ResourceUtil.getIdentifier(mView.getContext(), dataPoint.getIcon()));
            mTextTemperature.setText(String.valueOf(Math.round(dataPoint.getTemperature())) + (char) 0x00B0);
        }
    }
}

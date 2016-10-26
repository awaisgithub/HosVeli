package com.od.hrdf.event;

import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class EventListViewHolder extends RecyclerView.ViewHolder {

    PercentRelativeLayout parent = null;

    public final TextView title;
    public final TextView venue;
    public final TextView dateTime;
    public final SimpleDraweeView summaryImageView;

    public EventListViewHolder(View parentView, final PercentRelativeLayout parentLayout, TextView title, TextView venue, TextView dateTime, SimpleDraweeView summaryImageView) {
        super(parentView);
        this.parent = parentLayout;
        this.title = title;
        this.venue = venue;
        this.summaryImageView = summaryImageView;
        this.dateTime = dateTime;

    }

    public static EventListViewHolder newInstance(View parent) {

        PercentRelativeLayout parentLayout = (PercentRelativeLayout) parent.findViewById(R.id.parent_layout);
        TextView title = (TextView) parent.findViewById(R.id.title);
        TextView venue = (TextView) parent.findViewById(R.id.venue);
        TextView dateTime = (TextView) parent.findViewById(R.id.date_time);
        SimpleDraweeView summaryImageView = (SimpleDraweeView) parent.findViewById(R.id.header_image);

        return new EventListViewHolder(parent, parentLayout, title, venue, dateTime, summaryImageView);
    }

}

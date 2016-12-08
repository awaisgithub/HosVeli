package com.od.mma.event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.mma.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class EventListViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout parent = null;

    public final TextView title;
    public final TextView venue;
    public final TextView dateTime;
    public final ImageView share;
    public final ImageView favButton;
    public final SimpleDraweeView summaryImageView;

    public EventListViewHolder(View parentView, final RelativeLayout parentLayout, TextView title, TextView venue, TextView dateTime, SimpleDraweeView summaryImageView, ImageView share, ImageView favButton) {
        super(parentView);
        this.parent = parentLayout;
        this.title = title;
        this.venue = venue;
        this.summaryImageView = summaryImageView;
        this.share = share;
        this.favButton = favButton;
        this.dateTime = dateTime;
    }

    public static EventListViewHolder newInstance(View parent) {

        RelativeLayout parentLayout = (RelativeLayout) parent.findViewById(R.id.content_container);
        TextView title = (TextView) parent.findViewById(R.id.title);
        TextView venue = (TextView) parent.findViewById(R.id.venue);
        TextView dateTime = (TextView) parent.findViewById(R.id.date_time);
        ImageView share = (ImageView) parent.findViewById(R.id.share);
        ImageView favButton = (ImageView) parent.findViewById(R.id.button_fav);
        SimpleDraweeView summaryImageView = (SimpleDraweeView) parent.findViewById(R.id.header_image);

        return new EventListViewHolder(parent, parentLayout, title, venue, dateTime, summaryImageView, share, favButton);
    }

}

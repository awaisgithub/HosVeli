package com.od.mma.event.speaker.uploads;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.od.mma.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class SpeakerUploadListViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;

    public final TextView eventName;
    public final RecyclerView uploadsGrid;

    public SpeakerUploadListViewHolder(View parentView, final LinearLayout parentLayout, TextView sponsor_level_name, RecyclerView sponsorGrid) {
        super(parentView);
        this.parent = parentLayout;
        this.eventName = sponsor_level_name;
        this.uploadsGrid = sponsorGrid;

    }

    public static SpeakerUploadListViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView eventName = (TextView) parent.findViewById(R.id.event_name);
        RecyclerView uploadsGrid = (RecyclerView) parent.findViewById(R.id.uploads_grid);

        return new SpeakerUploadListViewHolder(parent, parentLayout, eventName, uploadsGrid);
    }


}

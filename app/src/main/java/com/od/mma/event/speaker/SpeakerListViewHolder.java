package com.od.mma.event.speaker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.mma.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class SpeakerListViewHolder extends RecyclerView.ViewHolder {

    CardView parent = null;

    public final TextView name;
    public final TextView designation;
    public final TextView overview;
    public final SimpleDraweeView speakerDrawee;
    public final CircleImageView speakerCircularImage = null;

    public SpeakerListViewHolder(View parentView, final CardView parentLayout, TextView name, TextView designation, TextView overview, SimpleDraweeView summaryImageView) {
        super(parentView);
        this.parent = parentLayout;
        this.name = name;
        this.designation = designation;
        this.speakerDrawee = summaryImageView;
        this.overview = overview;

    }

    public static SpeakerListViewHolder newInstance(View parent) {

        CardView parentLayout = (CardView) parent.findViewById(R.id.parent_layout);
        TextView name = (TextView) parent.findViewById(R.id.name);
        TextView designation = (TextView) parent.findViewById(R.id.designation);
        TextView overview = (TextView) parent.findViewById(R.id.overview);
        SimpleDraweeView summaryImageView = (SimpleDraweeView) parent.findViewById(R.id.speaker_photo);

        return new SpeakerListViewHolder(parent, parentLayout, name, designation, overview, summaryImageView);
    }

}

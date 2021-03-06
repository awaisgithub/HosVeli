package com.od.mma.event.exhibitor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.mma.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class EventExhibitorDetailViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;
    public final TextView name;
    public final TextView booth;
    public final ImageView externalLink;
    public final SimpleDraweeView imageView;

    public EventExhibitorDetailViewHolder(View parentView, final LinearLayout parentLayout, TextView name, SimpleDraweeView imageView, ImageView externalLink, TextView booth) {
        super(parentView);
        this.parent = parentLayout;
        this.name = name;
        this.booth = booth;
        this.imageView = imageView;
        this.externalLink = externalLink;
    }

    public static EventExhibitorDetailViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView name = (TextView) parent.findViewById(R.id.name);
        TextView booth = (TextView) parent.findViewById(R.id.booth);
        ImageView externalLink = (ImageView) parent.findViewById(R.id.external_link);
        SimpleDraweeView imageView = (SimpleDraweeView) parent.findViewById(R.id.exhibitor_photo);
        return new EventExhibitorDetailViewHolder(parent, parentLayout, name, imageView, externalLink, booth);
    }


}

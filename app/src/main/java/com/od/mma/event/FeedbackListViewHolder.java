package com.od.mma.event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.mma.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class FeedbackListViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;
    View dummyView = null;
    public final TextView title;
    public final TextView description;
    public final SimpleDraweeView imageView;

    public FeedbackListViewHolder(View parentView, final LinearLayout parentLayout, TextView title, TextView description, SimpleDraweeView summaryImageView) {
        super(parentView);
        this.parent = parentLayout;
        this.title = title;
        this.description = description;
        this.imageView = summaryImageView;

    }

    public static FeedbackListViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView title = (TextView) parent.findViewById(R.id.title);
        TextView description = (TextView) parent.findViewById(R.id.description);
        SimpleDraweeView summaryImageView = (SimpleDraweeView) parent.findViewById(R.id.image);
        return new FeedbackListViewHolder(parent, parentLayout, title, description, summaryImageView);
    }

}

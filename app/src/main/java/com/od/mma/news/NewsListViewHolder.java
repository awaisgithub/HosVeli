package com.od.mma.news;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.od.mma.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class NewsListViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;
    View dummyView = null;
    public final TextView title;
    public final TextView description;
    public final SimpleDraweeView summaryImageView;

    public NewsListViewHolder(View parentView, final LinearLayout parentLayout, TextView title, TextView description, SimpleDraweeView summaryImageView, View dummyView) {
        super(parentView);
        this.parent = parentLayout;
        this.title = title;
        this.description = description;
        this.summaryImageView = summaryImageView;
        this.dummyView = dummyView;

    }

    public static NewsListViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView title = (TextView) parent.findViewById(R.id.title);
        TextView description = (TextView) parent.findViewById(R.id.description);
        SimpleDraweeView summaryImageView = (SimpleDraweeView) parent.findViewById(R.id.summary_image);
        View dummyView = parent.findViewById(R.id.dummy_view);
        return new NewsListViewHolder(parent, parentLayout, title, description, summaryImageView, dummyView);
    }

}

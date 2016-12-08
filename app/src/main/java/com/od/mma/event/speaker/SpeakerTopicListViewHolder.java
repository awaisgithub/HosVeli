package com.od.mma.event.speaker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.od.mma.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class SpeakerTopicListViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;

    public final TextView title;
    public final WebView moreInfo;

    public SpeakerTopicListViewHolder(View parentView, final LinearLayout parentLayout, TextView title, WebView moreInfo) {
        super(parentView);
        this.parent = parentLayout;
        this.title = title;
        this.moreInfo = moreInfo;
    }

    public static SpeakerTopicListViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView title = (TextView) parent.findViewById(R.id.title);
        WebView moreInfo = (WebView) parent.findViewById(R.id.more_info);

        return new SpeakerTopicListViewHolder(parent, parentLayout, title, moreInfo);
    }

}

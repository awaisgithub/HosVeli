package com.od.hrdf.event.speaker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.R;

import de.hdodenhof.circleimageview.CircleImageView;

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

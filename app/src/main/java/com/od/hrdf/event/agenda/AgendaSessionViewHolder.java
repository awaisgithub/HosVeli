package com.od.hrdf.event.agenda;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.R;
import com.od.hrdf.event.exhibitor.EventExhibitorViewHolder;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class AgendaSessionViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;

    public final TextView title;
    public final TextView venue;
    public final TextView moreInfo;

    public AgendaSessionViewHolder(View parentView, final LinearLayout parentLayout, TextView title, TextView venue, TextView moreInfo) {
        super(parentView);
        this.parent = parentLayout;
        this.title = title;
        this.venue = venue;
        this.moreInfo = moreInfo;

    }

    public static AgendaSessionViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView title = (TextView) parent.findViewById(R.id.title);
        TextView venue = (TextView) parent.findViewById(R.id.venue);
        TextView moreInfo = (TextView) parent.findViewById(R.id.more_info);

        return new AgendaSessionViewHolder(parent, parentLayout, title, venue, moreInfo);
    }
}

package com.od.hrdf.event.exhibitor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.R;
import com.od.hrdf.event.agenda.AgendaSessionViewHolder;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class EventExhibitorViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;
    public final TextView title;
    public final ImageButton arrowButton;
    public final RecyclerView sessionListView;

    public EventExhibitorViewHolder(View parentView, final LinearLayout parentLayout, TextView title, RecyclerView sessionListView, ImageButton arrowButton) {
        super(parentView);
        this.parent = parentLayout;
        this.title = title;
        this.sessionListView = sessionListView;
        this.arrowButton = arrowButton;
    }

    public static EventExhibitorViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView title = (TextView) parent.findViewById(R.id.title);
        RecyclerView sessionListView = (RecyclerView) parent.findViewById(R.id.exhibitor_rcv);
        ImageButton arrowButton = (ImageButton) parent.findViewById(R.id.button_expand);
        return new EventExhibitorViewHolder(parent, parentLayout, title, sessionListView, arrowButton);
    }
}

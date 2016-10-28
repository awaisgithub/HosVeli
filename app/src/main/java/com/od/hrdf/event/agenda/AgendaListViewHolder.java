package com.od.hrdf.event.agenda;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class AgendaListViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;

    public final TextView startTime;
    public final TextView endTime;
    public final TextView title;
    public final ImageButton arrowButton;
    public final RecyclerView sessionListView;

    public AgendaListViewHolder(View parentView, final LinearLayout parentLayout, TextView title, TextView startTime, TextView endTime, RecyclerView sessionListView, ImageButton arrowButton) {
        super(parentView);
        this.parent = parentLayout;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionListView = sessionListView;
        this.arrowButton = arrowButton;
    }

    public static AgendaListViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView title = (TextView) parent.findViewById(R.id.title);
        TextView startTime = (TextView) parent.findViewById(R.id.start_time);
        TextView endTime = (TextView) parent.findViewById(R.id.end_time);
        RecyclerView sessionListView = (RecyclerView) parent.findViewById(R.id.agenda_session_rcv);
        ImageButton arrowButton = (ImageButton) parent.findViewById(R.id.button_expand);
        return new AgendaListViewHolder(parent, parentLayout, title, startTime, endTime, sessionListView, arrowButton);
    }

}

package com.od.mma.event.agenda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.mma.BOs.SessionItem;
import com.od.mma.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class AgendaSessionAdapter extends RealmRecyclerViewAdapter<SessionItem, RecyclerView.ViewHolder> {
    private Context context;
    private OrderedRealmCollection<SessionItem> data;
    private View view;

    public AgendaSessionAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<SessionItem> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_agenda_session, parent, false);
        return AgendaSessionViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AgendaSessionViewHolder viewHolder = (AgendaSessionViewHolder) holder;
        SessionItem session = data.get(position);
        viewHolder.venue.setText(session.getVenue());
        viewHolder.title.setText(session.getTitle());
        viewHolder.moreInfo.setText(session.getMoreinfo());

    }
}

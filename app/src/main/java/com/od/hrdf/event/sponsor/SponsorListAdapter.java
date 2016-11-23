package com.od.hrdf.event.sponsor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.hrdf.BOs.EventSponsor;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.event.EventListAdapterInterface;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class SponsorListAdapter extends RealmRecyclerViewAdapter<EventSponsor, RecyclerView.ViewHolder> {
    private Context context;
    private OrderedRealmCollection<EventSponsor> data;
    private View view;

    public SponsorListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<EventSponsor> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_sponsor_list, parent, false);
        return SponsorListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SponsorListViewHolder viewHolder = (SponsorListViewHolder) holder;
        final EventSponsor sponsor = data.get(position);
        viewHolder.sponsor_level_name.setText(sponsor.getSponsorlevelname());
        viewHolder.sponsorGrid.setLayoutManager(new GridLayoutManager(context, 2));
        SponsorGridAdapter sponsorGridAdapter = new SponsorGridAdapter(context, EventSponsor.getSponsorByLevelName(HRDFApplication.realm, sponsor.getSponsorlevelname()), true);
        viewHolder.sponsorGrid.setAdapter(sponsorGridAdapter);
    }
}

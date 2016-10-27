package com.od.hrdf.event.sponsor;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.R;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class SponsorListViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;

    public final TextView sponsor_level_name;
    public final RecyclerView sponsorGrid;

    public SponsorListViewHolder(View parentView, final LinearLayout parentLayout, TextView sponsor_level_name, RecyclerView sponsorGrid) {
        super(parentView);
        this.parent = parentLayout;
        this.sponsor_level_name = sponsor_level_name;
        this.sponsorGrid = sponsorGrid;

    }

    public static SponsorListViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView sponsor_level_name = (TextView) parent.findViewById(R.id.sponsor_level_name);
        RecyclerView sponsorGrid = (RecyclerView) parent.findViewById(R.id.sponsor_grid);

        return new SponsorListViewHolder(parent, parentLayout, sponsor_level_name, sponsorGrid);
    }


}

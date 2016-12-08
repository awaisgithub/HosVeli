package com.od.mma.abouts;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.od.mma.BOs.Branches;
import com.od.mma.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class BranchesListAdapter extends RealmRecyclerViewAdapter<Branches, RecyclerView.ViewHolder> {
    private Context context;
    private OrderedRealmCollection<Branches> data;
    private View view;
    private BranchParentNotifier notifier;
    public BranchesListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Branches> data, BranchParentNotifier notifier, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
        this.notifier = notifier;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_branch_list, parent, false);
        return BranchListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BranchListViewHolder viewHolder = (BranchListViewHolder) holder;
        Branches branch = data.get(position);
        viewHolder.branchName.setText(branch.getName());
        viewHolder.managerTV.setText(branch.getBranchManager());
        viewHolder.managerTV.getCompoundDrawablesRelative()[0].setColorFilter(ContextCompat.getColor(context, R.color.colorOrange), PorterDuff.Mode.SRC_ATOP);
        viewHolder.phoneTV.setText(branch.getPhone());
        viewHolder.phoneTV.getCompoundDrawablesRelative()[0].setColorFilter(ContextCompat.getColor(context, R.color.colorTabs), PorterDuff.Mode.SRC_ATOP);
        viewHolder.phoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) view;
                notifier.dialNumber(tv.getText().toString());
            }
        });
        viewHolder.faxTV.setText(branch.getFax());
        viewHolder.faxTV.getCompoundDrawablesRelative()[0].setColorFilter(ContextCompat.getColor(context, R.color.colorTabs), PorterDuff.Mode.SRC_ATOP);
        viewHolder.emailTV.setText(branch.getBranchManagerEmail());
        viewHolder.emailTV.getCompoundDrawablesRelative()[0].setColorFilter(ContextCompat.getColor(context, R.color.colorTabs), PorterDuff.Mode.SRC_ATOP);
        viewHolder.addressTV.setText(branch.getAddress());
        viewHolder.addressTV.getCompoundDrawablesRelative()[0].setColorFilter(ContextCompat.getColor(context, R.color.colorTabs), PorterDuff.Mode.SRC_ATOP);
    }

    public interface BranchParentNotifier{
        public void dialNumber(String number);
    }
}

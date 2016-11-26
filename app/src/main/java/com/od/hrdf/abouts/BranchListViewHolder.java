package com.od.hrdf.abouts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.od.hrdf.R;

/**
 * Created by Mahmoor on 19/2/2016.
 */
public class BranchListViewHolder extends RecyclerView.ViewHolder {

    LinearLayout parent = null;

    public final TextView branchName;
    public final TextView managerTV;
    public final TextView phoneTV;
    public final TextView faxTV;
    public final TextView emailTV;
    public final TextView addressTV;

    public BranchListViewHolder(View parentView, final LinearLayout parentLayout, TextView branchName, TextView managerTV, TextView phoneTV, TextView faxTV, TextView emailTV, TextView addressTV) {
        super(parentView);
        this.parent = parentLayout;
        this.branchName = branchName;
        this.managerTV = managerTV;
        this.phoneTV = phoneTV;
        this.faxTV = faxTV;
        this.emailTV = emailTV;
        this.addressTV = addressTV;
    }

    public static BranchListViewHolder newInstance(View parent) {

        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.parent_layout);
        TextView managerTV = (TextView) parent.findViewById(R.id.manager_name);
        TextView phoneTV = (TextView) parent.findViewById(R.id.phone);
        TextView faxTV = (TextView) parent.findViewById(R.id.fax);
        TextView branchName = (TextView) parent.findViewById(R.id.branch_name);
        TextView emailTV = (TextView) parent.findViewById(R.id.email);
        TextView addressTV = (TextView) parent.findViewById(R.id.address);

        return new BranchListViewHolder(parent, parentLayout, branchName, managerTV, phoneTV, faxTV, emailTV, addressTV);
    }

}

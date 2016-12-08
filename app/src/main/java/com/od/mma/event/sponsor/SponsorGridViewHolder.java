package com.od.mma.event.sponsor;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.mma.R;

import io.realm.RealmViewHolder;


/**
 * Created by Mahmoor on 19/2/2016.
 */
public class SponsorGridViewHolder extends RealmViewHolder {

    public final TextView name;
    public final SimpleDraweeView photoView;


    public SponsorGridViewHolder(LinearLayout container) {
        super(container);
        this.name = (TextView) container.findViewById(R.id.sponsor_name);
        this.photoView = (SimpleDraweeView) container.findViewById(R.id.sponsor_photo);
    }

}

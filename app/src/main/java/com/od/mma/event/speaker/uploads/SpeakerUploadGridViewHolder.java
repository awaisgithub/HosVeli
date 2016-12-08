package com.od.mma.event.speaker.uploads;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.mma.R;

import io.realm.RealmViewHolder;


/**
 * Created by Mahmoor on 19/2/2016.
 */
public class SpeakerUploadGridViewHolder extends RealmViewHolder {

    public final TextView name;
    public final SimpleDraweeView photoView;
    public final RelativeLayout parent;


    public SpeakerUploadGridViewHolder(RelativeLayout container) {
        super(container);
        parent = container;
        this.name = (TextView) container.findViewById(R.id.file_name);
        this.photoView = (SimpleDraweeView) container.findViewById(R.id.file_photo);
    }

}

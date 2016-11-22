package com.od.hrdf.event.sponsor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.hrdf.BOs.EventSponsor;
import com.od.hrdf.BOs.Sponsor;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.event.EventListAdapterInterface;
import com.od.hrdf.event.agenda.WordUtils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class SponsorGridAdapter extends RealmRecyclerViewAdapter<EventSponsor, RecyclerView.ViewHolder> {
    private Context context;
    private RealmResults<EventSponsor> data;
    private View view;

    public SponsorGridAdapter(@NonNull Context context, @Nullable RealmResults<EventSponsor> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {

        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

        }

        @Override
        public void onFailure(String id, Throwable throwable) {

        }
    };



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = inflater.inflate(R.layout.adapter_sponsor_grid, parent, false);
        return new SponsorGridViewHolder((LinearLayout) v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SponsorGridViewHolder sponsorGridViewHolder = (SponsorGridViewHolder) holder;
        final EventSponsor eventSponsor = data.get(position);
        Sponsor sponsor = Sponsor.getSponsor(eventSponsor.getSponsor(), HRDFApplication.realm);
        if(sponsor == null)
            return;

        String titleString = sponsor.getName();
//        titleString = titleString.toLowerCase();
//        titleString = WordUtils.capitalize(titleString);
//        if(titleString.contains("-")) {
//            try {
//                int idx = titleString.indexOf("-");
//                char ch = Character.toTitleCase(titleString.charAt(idx + 1));
//                char[] myNameChars = titleString.toCharArray();
//                myNameChars[idx + 1] = ch;
//                titleString = String.valueOf(myNameChars);
//            } catch(Exception ex) {
//                ex.printStackTrace();
//            }
//        }
        sponsorGridViewHolder.name.setText(titleString);
        sponsorGridViewHolder.name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        sponsorGridViewHolder.name.setSingleLine(true);
        sponsorGridViewHolder.name.setMarqueeRepeatLimit(-1);
        sponsorGridViewHolder.name.setSelected(true);

        if(sponsor.getImage() != null) {
            String image = sponsor.getImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            sponsorGridViewHolder.photoView.setImageURI(uri);
            sponsorGridViewHolder.photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            sponsorGridViewHolder.photoView.setController(controller);
        }
//
        sponsorGridViewHolder.photoView.setTag(eventSponsor.getSponsorWebsite());
        sponsorGridViewHolder.photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webLink = (String) view.getTag();
                if(webLink != null && !webLink.isEmpty()) {
                    Intent intentWebLink = new Intent(Intent.ACTION_VIEW, Uri.parse(webLink));
                    context.startActivity(intentWebLink);
                }
            }
        });
    }
}

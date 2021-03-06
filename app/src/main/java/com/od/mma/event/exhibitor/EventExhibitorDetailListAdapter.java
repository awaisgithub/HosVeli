package com.od.mma.event.exhibitor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.mma.BOs.EventExhibitor;
import com.od.mma.BOs.Exhibitor;
import com.od.mma.MMAApplication;
import com.od.mma.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class EventExhibitorDetailListAdapter extends RealmRecyclerViewAdapter<EventExhibitor, RecyclerView.ViewHolder> {
    private Context context;
    private ExhibitorListParentInterface exhibitorListParentInterface;
    private OrderedRealmCollection<EventExhibitor> data;
    private View view;

    public EventExhibitorDetailListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<EventExhibitor> data, ExhibitorListParentInterface eventListAdapterInterface, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.exhibitorListParentInterface = eventListAdapterInterface;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_exhibitor_detail_list, parent, false);
        return EventExhibitorDetailViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventExhibitorDetailViewHolder viewHolder = (EventExhibitorDetailViewHolder) holder;
        final EventExhibitor eventExhibitor = data.get(position);
        final Exhibitor exhibitor = Exhibitor.getExhibitor(eventExhibitor.getExhibitor(), MMAApplication.realm);
        if(exhibitor == null)
            return;

        viewHolder.name.setText(exhibitor.getName());
        viewHolder.booth.setText("Booth No. "+eventExhibitor.getBoothNo());

        viewHolder.externalLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentWebLink = new Intent(Intent.ACTION_VIEW, Uri.parse(exhibitor.getWebsite()));
                context.startActivity(intentWebLink);
            }
        });

        if(exhibitor.getImage() != null) {
            String image = exhibitor.getImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            viewHolder.imageView.setImageURI(uri);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            viewHolder.imageView.setController(controller);
        }

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exhibitorListParentInterface.showDetail(exhibitor.getImage(), eventExhibitor.getDescription(),  exhibitor.getName());
            }
        });

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
}

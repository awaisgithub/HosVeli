package com.od.mma.event.speaker;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.mma.BOs.EventSpeaker;
import com.od.mma.BOs.Speaker;
import com.od.mma.MMAApplication;
import com.od.mma.R;
import com.od.mma.event.EventListAdapterInterface;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class SpeakerListAdapter extends RealmRecyclerViewAdapter<EventSpeaker, RecyclerView.ViewHolder> {
    private Context context;
    private EventListAdapterInterface eventListAdapterInterface;
    private OrderedRealmCollection<EventSpeaker> data;
    private View view;

    public SpeakerListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<EventSpeaker> data, EventListAdapterInterface eventListAdapterInterface, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.eventListAdapterInterface = eventListAdapterInterface;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_speaker_list, parent, false);
        return SpeakerListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SpeakerListViewHolder viewHolder = (SpeakerListViewHolder) holder;
        final EventSpeaker eventSpeaker = data.get(position);
        Speaker speaker = Speaker.getSpeaker(eventSpeaker.getSpeaker(), MMAApplication.realm);
        if(speaker == null)
            return;

        viewHolder.name.setText(speaker.getName());
        viewHolder.designation.setText(speaker.getJobTitle());
        viewHolder.overview.setText(speaker.getOrganization());
        if(speaker.getImage() != null) {
            String image = speaker.getImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            viewHolder.speakerDrawee.setImageURI(uri);
            viewHolder.speakerDrawee.setScaleType(ImageView.ScaleType.FIT_XY);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            viewHolder.speakerDrawee.setController(controller);
        }

        viewHolder.parent.setTag(speaker.getId());
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventListAdapterInterface.gotoDetailActivity(((String)view.getTag()));
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

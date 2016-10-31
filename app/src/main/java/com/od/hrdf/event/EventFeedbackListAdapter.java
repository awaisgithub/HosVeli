package com.od.hrdf.event;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.ParseException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.hrdf.BOs.Event;
import com.od.hrdf.BOs.UserEvent;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class EventFeedbackListAdapter extends RealmRecyclerViewAdapter<Event, RecyclerView.ViewHolder> {
    private Context context;
    private EventListAdapterInterface eventListAdapterInterface;
    private OrderedRealmCollection<Event> data;
    private View view;

    public EventFeedbackListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Event> data, EventListAdapterInterface eventListAdapterInterface, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.eventListAdapterInterface = eventListAdapterInterface;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_event_feedback_list, parent, false);
        return FeedbackListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeedbackListViewHolder viewHolder = (FeedbackListViewHolder) holder;
        Event event = data.get(position);
        viewHolder.title.setText(event.getTitle());
        viewHolder.description.setText(R.string.event_feedback_item_message);

        if (event.getImage() != null) {
            String image = event.getImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            viewHolder.imageView.setImageURI(uri);
            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            viewHolder.imageView.setController(controller);
        }

        viewHolder.parent.setTag(event.getId());
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventListAdapterInterface.performOperationOnEvent(EventListAdapterInterface.EventOP.LAUNCH_ACTIVITY, ((String) view.getTag()));
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

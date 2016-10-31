package com.od.hrdf.event.exhibitor;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.ParseException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import com.od.hrdf.BOs.EventExhibitor;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.event.EventListAdapterInterface;
import com.od.hrdf.event.EventListViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class EventExhibitorListAdapter extends RealmRecyclerViewAdapter<EventExhibitor, RecyclerView.ViewHolder> {
    private Context context;
    private OrderedRealmCollection<EventExhibitor> data;
    private View view;

    public EventExhibitorListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<EventExhibitor> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_event_exhibitor, parent, false);
        return EventExhibitorViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final EventExhibitorViewHolder viewHolder = (EventExhibitorViewHolder) holder;
        final EventExhibitor eventExhibitor = data.get(position);
        viewHolder.title.setText(eventExhibitor.getExhibitorcategoryname());
        RealmResults realmResults = EventExhibitor.getExhibitorForCategory(HRDFApplication.realm, eventExhibitor.getExhibitorCategoryName(), eventExhibitor.getEvent());
        EventExhibitorDetailListAdapter eventExhibitorDetailListAdapter = new EventExhibitorDetailListAdapter(context, realmResults, null, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        viewHolder.sessionListView.setLayoutManager(linearLayoutManager);
        viewHolder.sessionListView.setAdapter(eventExhibitorDetailListAdapter);

        viewHolder.parent.setTag(false);
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExpanded = (boolean) view.getTag();
                if(isExpanded) {
                    view.setTag(false);
                    viewHolder.sessionListView.setVisibility(View.GONE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expand);
                } else {
                    view.setTag(true);
                    viewHolder.sessionListView.setVisibility(View.VISIBLE);
                    viewHolder.arrowButton.setImageResource(R.drawable.expanded);
                }
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

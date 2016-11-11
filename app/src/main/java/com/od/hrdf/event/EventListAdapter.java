package com.od.hrdf.event;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.ParseException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.news.NewsListViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static android.R.attr.order;
import static com.od.hrdf.R.id.imageView;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class EventListAdapter extends RealmRecyclerViewAdapter<Event, RecyclerView.ViewHolder> {
    private Context context;
    private EventListAdapterInterface eventListAdapterInterface;
    private OrderedRealmCollection<Event> data;
    private View view;

    public EventListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Event> data, EventListAdapterInterface eventListAdapterInterface, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.eventListAdapterInterface = eventListAdapterInterface;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_event_list, parent, false);
        return EventListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventListViewHolder viewHolder = (EventListViewHolder) holder;
        final Event event = data.get(position);
        viewHolder.title.setText(event.getTitle());
        viewHolder.venue.setText(event.getLocation());

        String startDateTime = null;
        try {
            startDateTime = (String) DateFormat.format("EEE, MMM dd", event.getStartDate());
            SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
            Date timeObj = formatter.parse(event.getStartTime());
            String time = new SimpleDateFormat("hh:mm a").format(timeObj);
            time = time.replace(".", "");
            startDateTime = startDateTime + ", " + time;
        } catch (ParseException e) {
            e.printStackTrace();
            startDateTime = "";
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            startDateTime = "";
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            startDateTime = "";
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
            startDateTime = "";
        }

        viewHolder.dateTime.setText(startDateTime);
        if (event.getImage() != null) {
            String image = event.getImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            viewHolder.summaryImageView.setImageURI(uri);
            viewHolder.summaryImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            viewHolder.summaryImageView.setController(controller);
        }

//        viewHolder.summaryImageView.setTag(event.getId());
//        viewHolder.summaryImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i(HRDFConstants.TAG, "Item clicked eventId="+event.getId());
//                eventListAdapterInterface.performOperationOnEvent(EventListAdapterInterface.EventOP.LAUNCH_ACTIVITY, event.getId());
//            }
//        });

        viewHolder.parent.setTag(event.getId());
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventListAdapterInterface.gotoDetailActivity(((String) view.getTag()));
            }
        });

        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eventListAdapterInterface.performOperationOnEvent(EventListAdapterInterface.EventOP.SHARE_SOCIAL, event.getId());
            }
        });

        if (event.getFavourite())
            viewHolder.favButton.setImageResource(R.drawable.bookmark_filled);
        else
            viewHolder.favButton.setImageResource(R.drawable.bookmark);

        viewHolder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event.getFavourite()) {
                    ((ImageView) view).setImageResource(R.drawable.bookmark);
                    eventListAdapterInterface.performOperationOnEvent(EventListAdapterInterface.EventOP.UNMARK_FAV, event.getId());
                } else {
                    ((ImageView) view).setImageResource(R.drawable.bookmark_filled);
                    eventListAdapterInterface.performOperationOnEvent(EventListAdapterInterface.EventOP.MARK_FAV, event.getId());
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

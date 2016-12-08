package com.od.mma.event.speaker.uploads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.mma.BOs.Document;
import com.od.mma.BOs.Event;
import com.od.mma.BOs.SpeakerDocument;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static com.od.mma.MMAApplication.realm;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class SpeakerUploadListAdapter extends RealmRecyclerViewAdapter<SpeakerDocument, RecyclerView.ViewHolder> {
    private Context context;
    private OrderedRealmCollection<SpeakerDocument> data;
    private View view;
    private SpeakerDocNotifier notifier;

    public SpeakerUploadListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<SpeakerDocument> data, SpeakerDocNotifier notifier, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
        this.notifier = notifier;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_speaker_upload_list, parent, false);
        return SpeakerUploadListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SpeakerUploadListViewHolder viewHolder = (SpeakerUploadListViewHolder) holder;
        final SpeakerDocument speakerDocument = data.get(position);
        if(speakerDocument == null)
            return;

        viewHolder.eventName.setText(Event.getEventName(speakerDocument.getEvent(), realm));
        viewHolder.uploadsGrid.setLayoutManager(new GridLayoutManager(context, 2));
        Log.i(MMAConstants.TAG, "speakerDocument.getSpeakerDocuments() = "+speakerDocument.getSpeakerDocuments().size());
        SpeakerUploadGridAdapter uploadsGridAdapter = new SpeakerUploadGridAdapter(context, Document.getAllDocumentForId(realm, speakerDocument.getId()), notifier, true);
        viewHolder.uploadsGrid.setAdapter(uploadsGridAdapter);
    }
}

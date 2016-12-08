package com.od.mma.event.speaker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.mma.BOs.SpeakerTopic;
import com.od.mma.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by MuhammadMahmoor on 10/26/16.
 */

public class SpeakerTopicListAdapter extends RealmRecyclerViewAdapter<SpeakerTopic, RecyclerView.ViewHolder> {
    private Context context;
    private OrderedRealmCollection<SpeakerTopic> data;
    private View view;

    public SpeakerTopicListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<SpeakerTopic> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_speaker_topic, parent, false);
        return SpeakerTopicListViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SpeakerTopicListViewHolder viewHolder = (SpeakerTopicListViewHolder) holder;
        final SpeakerTopic speakerTopic = data.get(position);
        viewHolder.title.setText(speakerTopic.getTitle());
       // viewHolder.title.setTypeface(face);
        viewHolder.moreInfo.setBackgroundColor(0x00000000);
        viewHolder.moreInfo.getSettings().setStandardFontFamily("sans-serif");
        viewHolder.moreInfo.getSettings().setFixedFontFamily("sans-serif");
        viewHolder.moreInfo.getSettings().setFantasyFontFamily("sans-serif");
        viewHolder.moreInfo.getSettings().setCursiveFontFamily("sans-serif");
        viewHolder.moreInfo.getSettings().setSerifFontFamily("sans-serif");
        viewHolder.moreInfo.getSettings().setSansSerifFontFamily("sans-serif");
        viewHolder.moreInfo.getSettings().setDefaultFontSize(14);
        viewHolder.moreInfo.loadData(speakerTopic.getDesc(), "text/html; charset=utf-8", "UTF-8");
        //viewHolder.moreInfo.setText(speakerTopic.getDesc());
        //viewHolder.moreInfo.setTypeface(face);
    }
}

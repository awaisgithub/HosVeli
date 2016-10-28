package com.od.hrdf.event.speaker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.Event;
import com.od.hrdf.BOs.SpeakerTopic;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.R;
import com.od.hrdf.event.EventFragment;
import com.od.hrdf.event.EventListAdapter;

import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.hrdf.HRDFApplication.realm;

public class SpeakerTopicsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String eventId;
    private String speakerId;
    private View rootView;

    public SpeakerTopicsFragment() {
    }

    public static SpeakerTopicsFragment newInstance(String eventId, String speakerId) {
        SpeakerTopicsFragment fragment = new SpeakerTopicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, eventId);
        args.putString(ARG_PARAM2, speakerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_PARAM1);
            speakerId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_speaker_topics, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        fetchSpeakerTopics();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.speaker_topic_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        RealmResults realmResults = null;

        realmResults = SpeakerTopic.getSpeakerTopicForEvent(realm, speakerId, eventId);

        SpeakerTopicListAdapter speakerTopicListAdapter = new SpeakerTopicListAdapter(getActivity(), realmResults, true);
        recyclerView.setAdapter(speakerTopicListAdapter);

    }

    private void fetchSpeakerTopics() {
        RealmQuery query = realm.where(SpeakerTopic.class).equalTo("event", eventId).equalTo("speaker", speakerId);
        SpeakerTopic.fetchEventSpeakerTopic(getActivity(), realm, Api.urlEventSpeakerTopic(eventId, speakerId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
    }
}

package com.od.mma.event.speaker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.od.mma.API.Api;
import com.od.mma.BOs.SpeakerTopic;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.R;

import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.mma.MMAApplication.realm;

public class SpeakerTopicsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String eventId;
    private String speakerId;
    private View rootView;
    private RealmResults realmResults = null;
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

        realmResults = SpeakerTopic.getSpeakerTopicForEvent(realm, speakerId, eventId);
        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults element) {
                if(element.size() > 0) {
                    hideMessage();
                }
            }
        });

        if(realmResults.size() < 1) {
            showMessage(R.string.sp_topics_not_available);
        }

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

                if(realmResults.size() < 1) {
                    showMessage(R.string.server_error);
                }
            }
        });
    }

    private void showMessage(int message){
        RelativeLayout messageLayout = (RelativeLayout) rootView.findViewById(R.id.error_layout);
        TextView messageView = (TextView) rootView.findViewById(R.id.label);
        messageView.setText(message);
        messageLayout.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        RelativeLayout messageLayout = (RelativeLayout) rootView.findViewById(R.id.error_layout);
        messageLayout.setVisibility(View.GONE);
    }
}

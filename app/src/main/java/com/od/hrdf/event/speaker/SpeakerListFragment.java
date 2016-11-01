package com.od.hrdf.event.speaker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.Event;
import com.od.hrdf.BOs.EventSpeaker;
import com.od.hrdf.BOs.Speaker;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.event.EventFragment;
import com.od.hrdf.event.EventListAdapter;
import com.od.hrdf.event.EventListAdapterInterface;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SpeakerListFragment extends Fragment implements EventListAdapterInterface {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private View rootView;
    private Realm realm;
    private String eventId;

    public SpeakerListFragment() {
        // Required empty public constructor
    }

    public static SpeakerListFragment newInstance(String param1) {
        SpeakerListFragment fragment = new SpeakerListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_speaker_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        initViews();
        fetchEventSpeakers();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.speaker_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        RealmResults realmResults = null;
        realmResults = EventSpeaker.getEventSpeakerResultSet(realm, eventId);
        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults element) {
                if (element.size() > 0) {
                    hideMessage();
                } else {
                    showMessage(R.string.sp_not_available);
                }
            }
        });

        if(realmResults.size() < 1) {
            showMessage(R.string.sp_not_available);
        }

        SpeakerListAdapter eventListAdapter = new SpeakerListAdapter(getActivity(), realmResults, this, true);
        recyclerView.setAdapter(eventListAdapter);
    }

    private void fetchEventSpeakers() {

        RealmQuery query = realm.where(EventSpeaker.class).equalTo("event", eventId);
        EventSpeaker.fetchEventSpeakers(getActivity(), realm, Api.urlEventSpeakerList(eventId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                HRDFApplication.realm.beginTransaction();
                Event.getEvent(eventId, realm).setSpeakers(EventSpeaker.getEventSpeakerList(realm, eventId));
                HRDFApplication.realm.commitTransaction();
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });

    }

    @Override
    public void gotoDetailActivity(String id) {
        Intent intent = new Intent(getActivity(), SpeakerDetailActivity.class);
        intent.putExtra(HRDFConstants.KEY_SPEAKER_ID, id);
        intent.putExtra(HRDFConstants.KEY_EVENT_ID, eventId);
        startActivity(intent);
    }

    @Override
    public void performOperationOnEvent(EventOP op, String eventId) {

    }

    private void showMessage(int message) {
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

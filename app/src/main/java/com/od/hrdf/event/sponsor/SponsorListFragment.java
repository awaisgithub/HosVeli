package com.od.hrdf.event.sponsor;


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
import com.od.hrdf.BOs.EventSpeaker;
import com.od.hrdf.BOs.EventSponsor;
import com.od.hrdf.BOs.Sponsor;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.event.EventListAdapterInterface;
import com.od.hrdf.event.speaker.SpeakerListAdapter;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SponsorListFragment extends Fragment implements EventListAdapterInterface {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View rootView;
    private String eventId;

    public SponsorListFragment() {
        // Required empty public constructor
    }

    public static SponsorListFragment newInstance(String param1) {
        SponsorListFragment fragment = new SponsorListFragment();
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
        rootView = inflater.inflate(R.layout.fragment_sponsor_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        fetchEventSponsor();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.sponsor_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        RealmResults realmResults = null;
        realmResults = EventSponsor.getDistinctByLevelSeq(HRDFApplication.realm, eventId);

        SponsorListAdapter sponsorListAdapter = new SponsorListAdapter(getActivity(), realmResults, this, true);
        recyclerView.setAdapter(sponsorListAdapter);

    }

    private void fetchEventSponsor() {
        RealmQuery query = HRDFApplication.realm.where(EventSponsor.class).equalTo("event", eventId);
        EventSponsor.fetchEventSponsors(getActivity(), HRDFApplication.realm, Api.urlEventSponsorsList(eventId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                HRDFApplication.realm.beginTransaction();
                Event.getEvent(eventId, HRDFApplication.realm).setSponsors(EventSponsor.getEventSponsorList(HRDFApplication.realm, eventId));
                HRDFApplication.realm.commitTransaction();
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
    }

    @Override
    public void gotoDetailActivity(String id) {

    }

    @Override
    public void socialMediaSharing(String id) {

    }
}

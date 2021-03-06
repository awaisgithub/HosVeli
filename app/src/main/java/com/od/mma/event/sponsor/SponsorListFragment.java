package com.od.mma.event.sponsor;


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
import com.od.mma.BOs.Event;
import com.od.mma.BOs.EventSponsor;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.MMAApplication;
import com.od.mma.R;

import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SponsorListFragment extends Fragment {
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
        realmResults = EventSponsor.getDistinctByLevelSeq(MMAApplication.realm, eventId);
        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults element) {
                if (element.size() > 0) {
                    hideMessage();
                } else {
                    showMessage(R.string.event_no_sponsor);
                }
            }
        });

        if(realmResults.size() < 1) {
            showMessage(R.string.event_no_sponsor);
        }

        SponsorListAdapter sponsorListAdapter = new SponsorListAdapter(getActivity(), realmResults, true);
        recyclerView.setAdapter(sponsorListAdapter);

    }

    private void fetchEventSponsor() {
        RealmQuery query = MMAApplication.realm.where(EventSponsor.class).equalTo("event", eventId);
        EventSponsor.fetchEventSponsors(getActivity(), MMAApplication.realm, Api.urlEventSponsorsList(eventId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                MMAApplication.realm.beginTransaction();
                Event.getEvent(eventId, MMAApplication.realm).setSponsors(EventSponsor.getEventSponsorList(MMAApplication.realm, eventId));
                MMAApplication.realm.commitTransaction();
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
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

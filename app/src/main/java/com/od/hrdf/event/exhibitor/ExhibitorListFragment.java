package com.od.hrdf.event.exhibitor;


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
import com.od.hrdf.BOs.EventExhibitor;
import com.od.hrdf.BOs.EventSpeaker;
import com.od.hrdf.BOs.Exhibitor;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.event.EventFragment;
import com.od.hrdf.event.EventListAdapter;
import com.od.hrdf.event.EventListAdapterInterface;

import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.hrdf.HRDFApplication.realm;

public class ExhibitorListFragment extends Fragment implements EventListAdapterInterface {
    private static final String ARG_PARAM1 = "param1";
    private String eventId;
    private String mParam2;
    private View rootView;

    public ExhibitorListFragment() {
    }

    public static ExhibitorListFragment newInstance(String eventId) {
        ExhibitorListFragment fragment = new ExhibitorListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, eventId);
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
        rootView = inflater.inflate(R.layout.fragment_exhibitor_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        fetchExhibitors();
    }

    private void initViews() {

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.exhibitor_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        RealmResults realmResults = null;
        realmResults = EventExhibitor.getEventExhibitor(HRDFApplication.realm, eventId);
        EventExhibitorListAdapter eventExhibitorListAdapter = new EventExhibitorListAdapter(getActivity(), realmResults, this, true);
        recyclerView.setAdapter(eventExhibitorListAdapter);
    }

    private void fetchExhibitors() {
        RealmQuery query= realm.where(EventExhibitor.class).equalTo("event", eventId);
        EventExhibitor.fetchEventExhibitors(getActivity(), realm, Api.urlEventExhibitorsList(eventId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                HRDFApplication.realm.beginTransaction();
                Event.getEvent(eventId, realm).setExhibitors(EventExhibitor.getEventExhibitorList(realm, eventId));
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

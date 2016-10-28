package com.od.hrdf.event;


import android.content.Context;
import android.content.Intent;
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
import com.od.hrdf.BOs.Organization;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.abouts.AboutUs;
import com.od.hrdf.landingtab.TabFragActivityInterface;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.hrdf.HRDFApplication.context;
import static com.od.hrdf.HRDFApplication.realm;

public class EventListFragment extends Fragment implements EventListAdapterInterface {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String type;
    private View rootView;
    private Realm realm;
    AboutUs aboutus;
    private TabFragActivityInterface mListener;

    public EventListFragment() {
    }

    public static EventListFragment newInstance(String type) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        aboutus = AboutUs.getAboutUs(realm);
        initViews();
        fetchAllEvents();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TabFragActivityInterface) {
            mListener = (TabFragActivityInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        RealmResults realmResults = null;
        if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_UPCOMING)) {
            realmResults = Event.getUpcomingEvents(realm);
        } else if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_ARCHIVE)) {
            realmResults = Event.getPastEvents(realm);
        }

        if (realmResults != null) {
            EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), realmResults, this, true);
            recyclerView.setAdapter(eventListAdapter);
        }
    }

    private void fetchAllEvents() {
        RealmQuery query = realm.where(Event.class);
        Event.fetchAllEvents(getActivity(), realm, Api.urlEventList(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULTLIST!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, " FAIL----FAIL-----FAIL== " + e.toString());
            }
        });
    }

    @Override
    public void gotoDetailActivity(String id) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(HRDFConstants.KEY_EVENT_ID, id);
        intent.putExtra(HRDFConstants.KEY_EVENT_TYPE, type);
        startActivity(intent);

    }

    @Override
    public void socialMediaSharing(String id) {
        Log.i("AWAIS1", " IT IS WORKING ");
        Log.i("AWAIS1", "LINK to SHARE =  " + aboutus.getSocialMediaShareLink());
        Log.i("AWAIS1", "TEXT to SHARE =  " + aboutus.getSocialMediaShareText());
        Log.i("AWAIS1", "PIC to SHARE =  " + aboutus.getSocialMediaSharePic());
        Intent textShareIntent = new Intent(Intent.ACTION_SEND);
        textShareIntent.putExtra(Intent.EXTRA_TEXT,aboutus.getSocialMediaShareText() + " \n" + aboutus.getSocialMediaShareLink() );
        textShareIntent.putExtra(Intent.EXTRA_SUBJECT, aboutus.getSocialMediaShareText() + "\n" + aboutus.getSocialMediaSharePic());
        textShareIntent.setType("text/plain");
        startActivity(Intent.createChooser(textShareIntent, "Share"));

    }


}

package com.od.mma.event.agenda;


import android.content.Context;
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

import com.od.mma.BOs.Agenda;
import com.od.mma.MMAApplication;
import com.od.mma.R;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AgendaListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;
    private String type;
    private String eventId;
    private AgendaFragActivityNotifier mListener;

    public AgendaListFragment() {
    }

    public static AgendaListFragment newInstance(String type, String eventId) {
        AgendaListFragment fragment = new AgendaListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
            eventId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_agenda_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AgendaFragActivityNotifier) {
            mListener = (AgendaFragActivityNotifier) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener.onFragmentNav(ProfileFragment.this, Util.Navigate.LOGOUT);
        mListener = null;
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.agenda_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        RealmResults realmResults = null;
        if (type.equalsIgnoreCase(AgendsMainActivity.AGENDA_DEC_SIX)) {
            realmResults = Agenda.getAgendaForDate(eventId, AgendsMainActivity.AGENDA_DEC_SIX, MMAApplication.realm);
        } else if (type.equalsIgnoreCase(AgendsMainActivity.AGENDA_DEC_SEVEN)) {
            realmResults = Agenda.getAgendaForDate(eventId, AgendsMainActivity.AGENDA_DEC_SEVEN, MMAApplication.realm);
        }

        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults element) {
                if (element.size() > 0) {
                    hideMessage();
                    if (mListener != null)
                        mListener.agendaAvailable();
                } else {
                    if (mListener != null)
                        mListener.noAgendaAvailable();

                    showMessage(R.string.event_no_agenda);
                }
            }
        });

        if (realmResults.size() < 1) {
            showMessage(R.string.event_no_agenda);
            if (mListener != null)
                mListener.noAgendaAvailable();
        }

        AgendaListAdapter agendaListAdapter = new AgendaListAdapter(getActivity(), realmResults, true);
        recyclerView.setAdapter(agendaListAdapter);
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

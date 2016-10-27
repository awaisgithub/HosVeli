package com.od.hrdf.event;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.od.hrdf.BOs.Event;
import com.od.hrdf.R;

public class EventInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View rootView;
    private Event event;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    public static EventInfoFragment newInstance(Event event) {
        EventInfoFragment fragment = new EventInfoFragment();
        fragment.event = event;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_info, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(event.getTitle());

        WebView overview = (WebView) rootView.findViewById(R.id.overview);
        overview.setBackgroundColor(0x00000000);
        overview.loadData(event.getOverview(), "text/html; charset=utf-8", "UTF-8");
    }
}

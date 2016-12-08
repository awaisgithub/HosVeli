package com.od.mma.event;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.od.mma.BOs.Event;
import com.od.mma.MMAApplication;
import com.od.mma.R;

public class EventInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String eventId;
    private String mParam2;
    private View rootView;
    private Event event;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    public static EventInfoFragment newInstance(String eventId) {
        EventInfoFragment fragment = new EventInfoFragment();
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
        rootView = inflater.inflate(R.layout.fragment_event_info, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        event = Event.getEvent(eventId, MMAApplication.realm);
        initViews();
    }

    private void initViews() {
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(event.getTitle());

        TextView objective = (TextView) rootView.findViewById(R.id.objective);
        objective.setText(event.getObjective());

        WebView overview = (WebView) rootView.findViewById(R.id.overview);
        overview.setBackgroundColor(0x00000000);
        //String htmlContent = "<!DOCTYPE html><html lang='en'><head><meta charset='utf-8'><title>HRDF EVENTS</title><meta name='viewport' content='width=device-width, initial-scale=1'><link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'></head><body style='background-color: transparent;color:#000000;font-weight:400;font-size:14px;font-color:#000000;'>"+event.getOverview()+"</body></html>";
        overview.getSettings().setStandardFontFamily("sans-serif");
        overview.getSettings().setFixedFontFamily("sans-serif");
        overview.getSettings().setFantasyFontFamily("sans-serif");
        overview.getSettings().setDefaultFontSize(14);
        overview.getSettings().setCursiveFontFamily("sans-serif");
        overview.getSettings().setSerifFontFamily("sans-serif");
        overview.getSettings().setSansSerifFontFamily("sans-serif");
        overview.loadData(event.getOverview(), "text/html; charset=utf-8", "UTF-8");
    }
}

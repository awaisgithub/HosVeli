package com.od.hrdf.event.speaker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.od.hrdf.BOs.Speaker;
import com.od.hrdf.R;

public class AboutSpeakerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Speaker speaker;
    private View rootView;
    public AboutSpeakerFragment() {
        // Required empty public constructor
    }

    public static AboutSpeakerFragment newInstance(Speaker speaker) {
        AboutSpeakerFragment fragment = new AboutSpeakerFragment();
        fragment.speaker = speaker;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about_speaker, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {

        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText("Who is "+speaker.getName()+" ?");

        WebView speakerDetail = (WebView) rootView.findViewById(R.id.speaker_detail);
        speakerDetail.loadData(speaker.getAboutSpeaker(), "text/html; charset=utf-8", "UTF-8");

    }
}

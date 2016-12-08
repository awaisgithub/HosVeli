package com.od.mma.event.speaker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.od.mma.BOs.Speaker;
import com.od.mma.R;

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
        speakerDetail.setBackgroundColor(0x00000000);

        //String htmlContent = "<!DOCTYPE html><html lang='en'><head><meta charset='utf-8'><title>HRDF EVENTS</title><meta name='viewport' content='width=device-width, initial-scale=1'><link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'></head><body style='background-color: transparent;color:#000000;font-family:Helvetica Neue;font-weight:400;font-size:14px;font-color:#000000;'>"+speaker.getAboutSpeaker()+"</body></html>";

        speakerDetail.getSettings().setStandardFontFamily("sans-serif");
        speakerDetail.getSettings().setFixedFontFamily("sans-serif");
        speakerDetail.getSettings().setFantasyFontFamily("sans-serif");
        speakerDetail.getSettings().setCursiveFontFamily("sans-serif");
        speakerDetail.getSettings().setSerifFontFamily("sans-serif");
        speakerDetail.getSettings().setSansSerifFontFamily("sans-serif");
        speakerDetail.getSettings().setDefaultFontSize(14);
//        String text = "<html><head>"
//        + "<style type=\'text/css\'>body{color: #000000; background-color: 0x00000000;}"
//        + "</style></head>"
//        + "<body>"
//                +speaker.getAboutSpeaker()
//        + "</body></html>";
        speakerDetail.loadData(speaker.getAboutSpeaker(), "text/html; charset=utf-8", "UTF-8");

    }
}

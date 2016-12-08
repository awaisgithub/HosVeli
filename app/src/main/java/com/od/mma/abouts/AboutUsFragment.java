package com.od.mma.abouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.od.mma.R;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class AboutUsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Realm realm;
    private WebView webView;
    private AboutUs aboutUs = null;
    private View rootView;
    private AboutUsParentNotifier mListener;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_about_uss, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        initViews();
        aboutUs = realm.where(AboutUs.class).findFirst();
        realm.addChangeListener(new RealmChangeListener<Realm>() {

            @Override
            public void onChange(Realm element) {
                aboutUs = realm.where(AboutUs.class).findFirst();
                if(aboutUs != null)
                    setInfo();
            }
        });

        if(aboutUs != null)
            setInfo();
    }

    private void initViews() {
        webView = (WebView) rootView.findViewById(R.id.web_view);
        webView.setBackgroundColor(0x00000000);
        webView.getSettings().setStandardFontFamily("sans-serif");
        webView.getSettings().setFixedFontFamily("sans-serif");
        webView.getSettings().setFantasyFontFamily("sans-serif");
        webView.getSettings().setCursiveFontFamily("sans-serif");
        webView.getSettings().setSerifFontFamily("sans-serif");
        webView.getSettings().setSansSerifFontFamily("sans-serif");
        webView.getSettings().setDefaultFontSize(14);
        webView.setVerticalScrollBarEnabled(false);
    }

    private void setInfo() {
        webView.loadData(aboutUs.getAboutUs(), "text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

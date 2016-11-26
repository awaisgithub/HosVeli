package com.od.hrdf.abouts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.API.Api;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.Utils.SectionsPagerAdapter;
import com.od.hrdf.event.EventFragment;
import com.od.hrdf.landingtab.TabFragActivityInterface;
import com.od.hrdf.landingtab.TabbarActivity;
import com.od.hrdf.news.NewsFragment;
import com.od.hrdf.profile.ProfileFragment;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AboutMainFragment extends Fragment implements View.OnClickListener {
    private TabFragActivityInterface mListener;
    private View rootView;
    private Realm realm;
    private RadioButton aboutUsRB, contactUs, offices;
    private AboutUs aboutUs = null;
    private ViewPager mViewPager;
    private SegmentedGroup segmentedGroup;

    public AboutMainFragment() {
        // Required empty public constructor
    }

    public static AboutMainFragment newInstance(String param1, String param2) {
        AboutMainFragment fragment = new AboutMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(HRDFConstants.TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        initViews();
        Log.i(HRDFConstants.TAG, "Default typeface = " + Typeface.DEFAULT);
        aboutUs = realm.where(AboutUs.class).findFirst();
        if (aboutUs != null) {
            setInfo();
        }

        fetchAboutUsInfo();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(HRDFConstants.TAG, "onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mViewPager != null)
        {
            int idx = mViewPager.getCurrentItem();
            Log.i(HRDFConstants.TAG, "onResume  idx="+idx);
            if (idx == 0)
                aboutUsRB.setChecked(true);
            else if(idx == 1)
                contactUs.setChecked(true);
            else
                offices.setChecked(true);

            mViewPager.setCurrentItem(idx, true);
        }
    }

    private void initViews() {

        segmentedGroup = (SegmentedGroup) rootView.findViewById(R.id.aboutus_segment_control);
        segmentedGroup.setTintColor(ContextCompat.getColor(getActivity(), R.color.colorTabs), Color.WHITE);
        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        aboutUsRB = (RadioButton) rootView.findViewById(R.id.aboutus);
        aboutUsRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0, true);
            }
        });

        contactUs = (RadioButton) rootView.findViewById(R.id.contactus);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1, true);
            }
        });

        offices = (RadioButton) rootView.findViewById(R.id.branches);
        offices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2, true);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    aboutUsRB.setChecked(true);
                else if(position == 1)
                    contactUs.setChecked(true);
                else
                    offices.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(mViewPager);

        // webView.getSettings().setJavaScriptEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(AboutUsFragment.newInstance("", ""), "About Us");
        adapter.addFragment(ContactUsFragment.newInstance("", ""), "Contact Us");
        adapter.addFragment(OfficeListFragment.newInstance("", ""), "Offices");
        viewPager.setAdapter(adapter);
    }

    private void fetchAboutUsInfo() {
        RealmQuery query = realm.where(AboutUs.class);
        String url = "http://" + Api.basicBaseURL + "/jw/web/json/plugin/org.od.webservice.JsonApiPlugin2/service?appId=hrdfApp&listId=hrdfAboutUs&action=list&imageUrl=Yes";
        AboutUs.fetchAboutUs(getActivity(), realm, url, query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                aboutUs = (AboutUs) fetchedItems.first();
                setInfo();
            }

            @Override
            public void fetchDidFail(Exception e) {
                if (aboutUs == null) {

                } else {
                    // do nothing
                }
            }
        });
    }

    private void setInfo() {

        SimpleDraweeView headerImageView = (SimpleDraweeView) rootView.findViewById(R.id.header_imageview);
        headerImageView.setImageURI(aboutUs.getAboutUsBanner());
        rootView.findViewById(R.id.button_call).setOnClickListener(this);
        rootView.findViewById(R.id.button_email).setOnClickListener(this);
        rootView.findViewById(R.id.button_website).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + aboutUs.getPhone()));
                startActivity(intent);
                break;
            case R.id.button_email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", aboutUs.getEmail(), null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.button_website:
                Intent intentWebLink = new Intent(Intent.ACTION_VIEW, Uri.parse(aboutUs.getWebsite()));
                startActivity(intentWebLink);
                break;
        }
    }
}

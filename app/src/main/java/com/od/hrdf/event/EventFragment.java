package com.od.hrdf.event;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class EventFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String LIST_TYPE_UPCOMING = "upcoming";
    public static final String LIST_TYPE_ARCHIVE = "archive";
    public static final String LIST_TYPE_FEEDBACK = "feedback";
    public static final String LIST_TYPE_MY_EVENTS = "myevets";
    public static final String LIST_TYPE_FAV_EVENTS = "favevets";
    private String mParam1;
    private String mParam2;
    private View rootView;
    private ViewPager mViewPager;
    public EventFragment() {
    }

    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(HRDFConstants.TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(HRDFConstants.TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_event, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(HRDFConstants.TAG, "onActivityCreated");
        initViews();
    }

    private void initViews() {
        final SegmentedGroup segmentedGroup = (SegmentedGroup) rootView.findViewById(R.id.event_segment_control);
        segmentedGroup.setTintColor(ContextCompat.getColor(getActivity(), R.color.colorTabs), Color.WHITE);
        ((RadioButton) segmentedGroup.findViewById(R.id.event_upcoming)).setOnClickListener(this);
        ((RadioButton) segmentedGroup.findViewById(R.id.event_archive)).setOnClickListener(this);
        ((RadioButton) segmentedGroup.findViewById(R.id.event_feedback)).setOnClickListener(this);
        mViewPager = (ViewPager) rootView.findViewById(R.id.event_pager);
        //mViewPager.setOffscreenPageLimit(4);
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton)segmentedGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.i(HRDFConstants.TAG, "setupViewPager");
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(EventListFragment.newInstance(LIST_TYPE_UPCOMING), "Upcoming");
        adapter.addFragment(EventListFragment.newInstance(LIST_TYPE_ARCHIVE), "Archive");
        adapter.addFragment(EventListFragment.newInstance(LIST_TYPE_FEEDBACK), "Feedback");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();
        switch (viewId) {
            case R.id.event_upcoming:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.event_archive:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.event_feedback:
                mViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

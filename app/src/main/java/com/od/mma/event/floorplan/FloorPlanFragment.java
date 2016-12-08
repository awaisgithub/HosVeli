package com.od.mma.event.floorplan;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.od.mma.API.Api;
import com.od.mma.BOs.Floorplan;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.R;
import com.od.mma.Utils.SectionsPagerAdapter;

import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.mma.MMAApplication.realm;

public class FloorPlanFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String eventId;
    private View rooView;
    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private SectionsPagerAdapter adapter;
    private Floorplan floor_plan;

    public FloorPlanFragment() {
    }

    public static FloorPlanFragment newInstance(String param1) {
        FloorPlanFragment fragment = new FloorPlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
        rooView = inflater.inflate(R.layout.fragment_floor_plan, container, false);
        return rooView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        viewPager = (ViewPager) rooView.findViewById(R.id.floor_plan_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                try {
                    floor_plan = (Floorplan) Floorplan.getFloorPlanForEvent(realm, eventId).get(position);
                    ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup = (RadioGroup) rooView.findViewById(R.id.radio_group);
        RealmResults realmResults = Floorplan.getFloorPlanForEvent(realm, eventId);
        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults element) {
                if (element.size() > 0) {
                    hideMessage();
                } else {
                    showMessage(R.string.event_no_floorplan);
                }
            }
        });

        if(realmResults.size() < 1) {
            showMessage(R.string.event_no_floorplan);
        } else {
            setupViewPager(realmResults);
        }

        fetchFloorPlan();
    }

    private void fetchFloorPlan() {
        RealmQuery query = realm.where(Floorplan.class).equalTo("event", eventId);
        Floorplan.fetchEventFloorPlan(getActivity(), realm, Api.urlEventFloorplan(eventId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                if(fetchedItems.size() > 0) {
                    hideMessage();
                    setupViewPager(fetchedItems);
                }
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
    }

    private void setupViewPager(RealmResults realmResults) {
        adapter = new SectionsPagerAdapter(getChildFragmentManager());
        radioGroup.removeAllViews();
        for (int i = 0; i < realmResults.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setClickable(false);
            radioGroup.addView(radioButton);
            Floorplan floorplan = (Floorplan) realmResults.get(i);
            adapter.addFragment(FloorImageFragment.newInstance(floorplan.getFloorPlan(), floorplan.getDesc()), "About Us");
        }

        if (radioGroup.getChildCount() > 0)
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);

        viewPager.setAdapter(adapter);
    }

    private void showMessage(int message) {
        RelativeLayout messageLayout = (RelativeLayout) rooView.findViewById(R.id.error_layout);
        TextView messageView = (TextView) rooView.findViewById(R.id.label);
        messageView.setText(message);
        messageLayout.setVisibility(View.VISIBLE);
        rooView.findViewById(R.id.pager_parent).setVisibility(View.GONE);
    }

    private void hideMessage() {
        RelativeLayout messageLayout = (RelativeLayout) rooView.findViewById(R.id.error_layout);
        messageLayout.setVisibility(View.GONE);
        rooView.findViewById(R.id.pager_parent).setVisibility(View.VISIBLE);
    }


}

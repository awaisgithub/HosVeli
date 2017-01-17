package com.od.mma.Membership;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.od.mma.R;
import com.od.mma.Utils.SectionsPagerAdapter;

public class MembershipFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View rooView;
    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private SectionsPagerAdapter adapter;

    public MembershipFragment() {
        // Required empty public constructor
    }

    public static MembershipFragment newInstance(String param1, String param2) {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rooView = inflater.inflate(R.layout.fragment_membership, container, false);
        return rooView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        Button registerButton = (Button) rooView.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent reg_membership = new Intent(getActivity(), RegMainFragActivity.class);
                startActivity(reg_membership);


            }
        });

//        viewPager = (PagerViewPager) rooView.findViewById(R.id.floor_plan_pager);
//        viewPager.addOnPageChangeListener(new PagerViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                try {
//                    ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
//                } catch (IndexOutOfBoundsException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        radioGroup = (RadioGroup) rooView.findViewById(R.id.radio_group);
//        setupViewPager();
    }

//    private void setupViewPager() {
//        marital_adapter = new SectionsPagerAdapter(getChildFragmentManager());
//        radioGroup.removeAllViews();
//        for (int i = 0; i < 2; i++) {
//            RadioButton radioButton = new RadioButton(getActivity());
//            radioButton.setClickable(false);
//            radioGroup.addView(radioButton);
//            marital_adapter.addFragment();
//            marital_adapter.addFragment(MembershipFragmentCategoryGridView.newInstance(floorplan.getFloorPlan(), floorplan.getDesc()), "About Us");
//        }
//
//        if (radioGroup.getChildCount() > 0)
//            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
//
//        viewPager.setAdapter(marital_adapter);
//    }

}

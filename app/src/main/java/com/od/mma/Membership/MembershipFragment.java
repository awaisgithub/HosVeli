package com.od.mma.Membership;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }


}

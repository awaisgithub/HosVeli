package com.od.hrdf.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.od.hrdf.BOs.User;
import com.od.hrdf.R;

import static com.od.hrdf.HRDFApplication.realm;

public class ProfileMyProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View rootView;
    TextView name;
    TextView email;
    TextView nationality;
    TextView contact_number;
    User user;

    public ProfileMyProfileFragment() {
    }

    public static ProfileMyProfileFragment newInstance(String param1, String param2) {
        ProfileMyProfileFragment fragment = new ProfileMyProfileFragment();
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
        rootView = inflater.inflate(R.layout.fragment_profile_my_profile, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = User.getCurrentUser(realm);
        name = (TextView) rootView.findViewById(R.id.profile_name);
        email = (TextView) rootView.findViewById(R.id.profile_email);
        nationality = (TextView) rootView.findViewById(R.id.profile_nationality);
        contact_number = (TextView) rootView.findViewById(R.id.profile_contact_no);
        name.setText(user.getName());
        email.setText(user.getId());
        nationality.setText(user.getNationality());
        contact_number.setText(user.getContactNumber());
    }
}

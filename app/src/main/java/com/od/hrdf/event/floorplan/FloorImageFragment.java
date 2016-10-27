package com.od.hrdf.event.floorplan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.BOs.Floorplan;
import com.od.hrdf.R;

public class FloorImageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String floorPlanImageURL;
    private String floorPlanDesc;
    private String mParam2;
    private View rootView;
    private Floorplan floorplan;
    public FloorImageFragment() {
        // Required empty public constructor
    }

    public static FloorImageFragment newInstance(String param1, String param2) {
        FloorImageFragment fragment = new FloorImageFragment();
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
            floorPlanImageURL = getArguments().getString(ARG_PARAM1);
            floorPlanDesc = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_floor_image, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        SimpleDraweeView floorPlanImage = (SimpleDraweeView) rootView.findViewById(R.id.floor_plan_image);
        if(floorPlanImageURL != null) {
            String image = floorPlanImageURL;
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            floorPlanImage.setImageURI(uri);
        }
    }
}

package com.od.hrdf.event.floorplan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.od.hrdf.BOs.Floorplan;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;

public class FloorImageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String floorPlanImageURL;
    private String floorPlanDesc;
    private String mParam2;
    private View rootView;
    private Floorplan floorplan;
    private SimpleDraweeView floorPlanPic;
    TextView floorName;
    public FloorImageFragment() {
        // Required empty public constructor
    }

    public String getFloorPlanImageURL() {
        return floorPlanImageURL;
    }

    public void setFloorPlanImageURL(String floorPlanImageURL) {
        this.floorPlanImageURL = floorPlanImageURL;
    }

    public String getFloorPlanDesc() {
        return floorPlanDesc;
    }

    public void setFloorPlanDesc(String floorPlanDesc) {
        this.floorPlanDesc = floorPlanDesc;
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
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        floorPlanPic = (SimpleDraweeView) rootView.findViewById(R.id.floor_plan_image);
        floorPlanPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(floorPlanImageURL!=null) {
                    Intent zoom = new Intent(getActivity(), FragmentFloorImageZoom.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("floor_plan_image_url", floorPlanImageURL);
                    bundle.putString("floor_plan_name", floorPlanDesc);
                    zoom.putExtras(bundle);
                    floorName = (TextView) rootView.findViewById(R.id.floor_plan_name);
                    floorName.setText(floorPlanDesc);
                    getActivity().startActivity(zoom);
                }
            }
        });

        floorName = (TextView) rootView.findViewById(R.id.floor_plan_name);
        floorName.setText(floorPlanDesc);

    }

    private void initViews() {
        SimpleDraweeView floorPlanImage = (SimpleDraweeView) rootView.findViewById(R.id.floor_plan_image);
        Log.i(HRDFConstants.TAG, "floorPlanImageURL = "+floorPlanImageURL);
        if (floorPlanImageURL != null) {
            String image = floorPlanImageURL;
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            floorPlanImage.setImageURI(uri);
            floorName = (TextView) rootView.findViewById(R.id.floor_plan_name);
            floorName.setText(floorPlanDesc);
        }

    }
}

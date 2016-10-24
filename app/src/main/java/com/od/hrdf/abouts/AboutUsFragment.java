package com.od.hrdf.abouts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.landingtab.TabFragActivityInterface;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AboutUsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TabFragActivityInterface mListener;
    private View rootView;
    private Realm realm;
    private WebView webView;
    private TextView address, contact, email, website;
    private RadioButton aboutUsRB;
    private AboutUs aboutUs = null;

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
        Log.i(HRDFConstants.TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        initViews();
        aboutUs = realm.where(AboutUs.class).findFirst();
        if (aboutUs != null)
            setInfo();

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

    private void initViews() {
        final ViewSwitcher viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.view_switcher);
        aboutUsRB = (RadioButton) rootView.findViewById(R.id.aboutus);
        aboutUsRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    viewSwitcher.setDisplayedChild(0);
            }
        });
        final RadioButton contactUs = (RadioButton) rootView.findViewById(R.id.contactus);

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.setDisplayedChild(1);
            }
        });

        webView = (WebView) rootView.findViewById(R.id.web_view);
        webView.setBackgroundColor(0x00000000);
        webView.getSettings().setJavaScriptEnabled(true);

        address = (TextView) rootView.findViewById(R.id.address);
        contact = (TextView) rootView.findViewById(R.id.contact);
        email = (TextView) rootView.findViewById(R.id.email);
        website = (TextView) rootView.findViewById(R.id.website);
    }

    private void fetchAboutUsInfo() {
        RealmQuery query = realm.where(AboutUs.class);
        String url = "http://www.mypams.net/jw/web/json/plugin/org.od.webservice.JsonApiPlugin2/service?appId=hrdfApp&listId=hrdfAboutUs&action=list&imageUrl=Yes";
        AboutUs.fetchAboutUs(getActivity(), realm, url, query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                aboutUs = (AboutUs) fetchedItems.first();
                setInfo();
            }

            @Override
            public void fetchDidFail(Exception e) {
                if (aboutUs == null) {
                    // TODO show error message
                } else {
                    // do nothing
                }
            }
        });
    }

    private void setInfo() {
        webView.loadData(aboutUs.getAboutUs(), "text/html; charset=utf-8", "UTF-8");
        SimpleDraweeView headerImageView = (SimpleDraweeView) rootView.findViewById(R.id.header_imageview);
        //ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.login_bg).build();
        headerImageView.setImageURI(aboutUs.getAboutUsBanner());

        address.setText(aboutUs.getAddress());
        contact.setText(aboutUs.getPhone());
        email.setText(aboutUs.getEmail());
        website.setText(aboutUs.getWebsite());
        final ViewSwitcher viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.view_switcher);
//        SegmentedGroup segmentedGroup = (SegmentedGroup) rootView.findViewById(R.id.profile_segment_control);
//        int checkedButtonId = segmentedGroup.getCheckedRadioButtonId();
//        switch (checkedButtonId) {
//            case R.id.aboutus:
//                viewSwitcher.setDisplayedChild(0);
//                break;
//            case R.id.contactus:
//                viewSwitcher.setDisplayedChild(1);
//                break;
//            default:
//                break;
//        }
        //aboutUsRB.setChecked(true);
    }

}

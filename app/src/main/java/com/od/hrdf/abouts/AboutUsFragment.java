package com.od.hrdf.abouts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

public class AboutUsFragment extends Fragment implements View.OnClickListener {
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

        SegmentedGroup segmentedGroup = (SegmentedGroup) rootView.findViewById(R.id.aboutus_segment_control);
        segmentedGroup.setTintColor(ContextCompat.getColor(getActivity(), R.color.colorLightBlue), Color.WHITE);
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

        rootView.findViewById(R.id.twitter).setOnClickListener(this);
        rootView.findViewById(R.id.facebook).setOnClickListener(this);
        rootView.findViewById(R.id.linkedin).setOnClickListener(this);
        rootView.findViewById(R.id.instagram).setOnClickListener(this);
        rootView.findViewById(R.id.youtube).setOnClickListener(this);

        rootView.findViewById(R.id.button_call).setOnClickListener(this);
        rootView.findViewById(R.id.button_email).setOnClickListener(this);
        rootView.findViewById(R.id.button_website).setOnClickListener(this);

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.twitter:
                Intent twitterIntent = getOpenTwitterIntent(getContext());
                startActivity(twitterIntent);
                break;
            case R.id.facebook:
                Intent facebookIntent = getOpenFacebookIntent(getContext());
                startActivity(facebookIntent);
                break;
            case R.id.linkedin:
                Intent linkedInIntent = getOpenLinkdinIntent(getContext());
                startActivity(linkedInIntent);
                break;
            case R.id.instagram:
                Intent instaIntent = getOpenInstagramIntent(getContext());
                startActivity(instaIntent);
                break;
            case R.id.youtube:
                Intent youtubeIntent = getOpenYouTubeIntent(getContext());
                startActivity(youtubeIntent);
                break;
            case R.id.button_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+aboutUs.getPhone()));
                startActivity(intent);
                break;
            case R.id.button_email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts( "mailto",aboutUs.getEmail(), null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.button_website:
                Intent intentWebLink = new Intent(Intent.ACTION_VIEW, Uri.parse(aboutUs.getWebsite()));
                startActivity(intentWebLink);
                break;
        }
    }

    public Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/"+aboutUs.getFacebookLink())); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/MY-HRDF-"+aboutUs.getFacebookLink())); //catches and opens a url to the desired page
        }
    }

    public Intent getOpenTwitterIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.twitter.android", 0); //Checks if Twitter is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/"+aboutUs.getTwitterLink())); //Trys to make intent with Twitter's's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/"+aboutUs.getTwitterLink())); //catches and opens a url to the desired page
        }
    }

    public Intent getOpenLinkdinIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.linkedin.android", 0); //Checks if Linkdin is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/company/"+aboutUs.getLinkedinLink())); //Trys to make intent with Linkdin's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/company/"+aboutUs.getLinkedinLink())); //catches and opens a url to the desired page
        }
    }

    public Intent getOpenInstagramIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.instagram.android", 0); //Checks if Instagram is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/"+aboutUs.getInstagramLink()+"/?hl=en")); //Trys to make intent with Instagram's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/"+aboutUs.getInstagramLink()+"/?hl=en")); //catches and opens a url to the desired page
        }
    }

    public Intent getOpenYouTubeIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.google.android.youtube", 0); //Checks if YT is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/"+aboutUs.getYoutubeLink())); //Trys to make intent with YT's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/"+aboutUs.getYoutubeLink())); //catches and opens a url to the desired page
        }
    }
}

package com.od.hrdf.abouts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.od.hrdf.R;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class ContactUsFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView address, contact, email, website;
    private View rootView;
    private Realm realm;
    private AboutUs aboutUs = null;
    private AboutUsParentNotifier mListener;
    public ContactUsFragment() {
        // Required empty public constructor
    }

    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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
        rootView = inflater.inflate(R.layout.contactus_layout, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        aboutUs = realm.where(AboutUs.class).findFirst();
        initViews();
        realm.addChangeListener(new RealmChangeListener<Realm>() {

            @Override
            public void onChange(Realm element) {
                aboutUs = realm.where(AboutUs.class).findFirst();
                if(aboutUs != null)
                    setInfo();
            }
        });

        if(aboutUs != null)
            setInfo();
    }

    private void initViews() {
        address = (TextView) rootView.findViewById(R.id.address);
        contact = (TextView) rootView.findViewById(R.id.contact);
        email = (TextView) rootView.findViewById(R.id.email);
        website = (TextView) rootView.findViewById(R.id.website);

        rootView.findViewById(R.id.twitter).setOnClickListener(this);
        rootView.findViewById(R.id.facebook).setOnClickListener(this);
        rootView.findViewById(R.id.linkedin).setOnClickListener(this);
        rootView.findViewById(R.id.instagram).setOnClickListener(this);
        rootView.findViewById(R.id.youtube).setOnClickListener(this);
    }

    private void setInfo() {
        address.setText(aboutUs.getAddress());
        contact.setText(aboutUs.getPhone());
        email.setText(aboutUs.getEmail());
        website.setText(aboutUs.getWebsite());
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

package com.od.hrdf.event;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.Event;
import com.od.hrdf.BOs.User;
import com.od.hrdf.BOs.UserEvent;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.abouts.AboutUs;
import com.od.hrdf.landingtab.TabFragActivityInterface;
import com.surveymonkey.surveymonkeyandroidsdk.SurveyMonkey;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class EventListFragment extends Fragment implements EventListAdapterInterface {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String type;
    private View rootView;
    private Realm realm;
    private AboutUs aboutus;
    private User user;
    private TabFragActivityInterface mListener;
    private RealmResults realmResults = null;
    SurveyMonkey sdkInstance = new SurveyMonkey();

    public EventListFragment() {
    }

    public static EventListFragment newInstance(String type) {
        Log.i(HRDFConstants.TAG, "EventListFragment = " + type);
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(HRDFConstants.TAG, "EventListFragment onCreate= " + type);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(HRDFConstants.TAG, "EventListFragment onCreateView= " + type);
        rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(HRDFConstants.TAG, "EventListFragment onActivityCreated= " + type);
        realm = Realm.getDefaultInstance();
        aboutus = AboutUs.getAboutUs(realm);
        user = User.getCurrentUser(realm);
        initViews();
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

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.i(HRDFConstants.TAG, "initViews = " + type);

        if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_UPCOMING)) {

            realmResults = Event.getUpcomingEvents(realm);
            if (realmResults.size() < 1) {
                showMessage(R.string.event_upcoming_not_availble);
            }
            EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), realmResults, this, true);
            recyclerView.setAdapter(eventListAdapter);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    fetchAllEvents();
                    //fetchUserEvents();
                }
            });
        } else if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_ARCHIVE)) {
            realmResults = Event.getPastEvents(realm);
            if (realmResults.size() < 1) {
                showMessage(R.string.event_archive_not_availble);
            }
            EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), realmResults, this, true);
            recyclerView.setAdapter(eventListAdapter);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    fetchAllEvents();
                }
            });
        } else if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_MY_EVENTS)) {
            realmResults = UserEvent.getAllUserEvents(realm, user.getId());
            if (realmResults.size() < 1) {
                showMessage(R.string.event_not_book);
            } else {
            }

            UserEventListAdapter userEventListAdapter = new UserEventListAdapter(getActivity(), realmResults, this, true);
            recyclerView.setAdapter(userEventListAdapter);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    fetchUserEvents();
                }
            });
        } else if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_FAV_EVENTS)) {
            realmResults = Event.getUpFavEvents(realm);
            if (realmResults.size() < 1) {
                showMessage(R.string.event_not_fav);
            }
            EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), realmResults, this, true);
            recyclerView.setAdapter(eventListAdapter);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    fetchAllEvents();
                }
            });
        } else if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_FEEDBACK)) {
            realmResults = Event.getSurveyEvents(realm);
            if (realmResults.size() < 1) {
                showMessage(R.string.event_feedback_not_availble);
            }
            EventFeedbackListAdapter eventFeedbackListAdapter = new EventFeedbackListAdapter(getActivity(), realmResults, this, true);
            recyclerView.setAdapter(eventFeedbackListAdapter);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    fetchAllEvents();
                }
            });
        }

        realmResults.addChangeListener(new RealmChangeListener<RealmResults>() {
            @Override
            public void onChange(RealmResults element) {
                if (element.size() > 0) {
                    hideMessage();
                } else {
                    if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_FAV_EVENTS)) {
                        showMessage(R.string.event_not_fav);
                    }
                }
            }
        });
    }

    private void fetchAllEvents() {
        RealmQuery query = realm.where(Event.class);
        Event.fetchAllEvents(getActivity(), realm, Api.urlEventList(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_UPCOMING))
                    fetchUserEvents();
            }

            @Override
            public void fetchDidFail(Exception e) {
                if (realmResults.size() < 1) {
                    showMessage(R.string.server_error);
                } else {
                    if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_UPCOMING))
                        fetchUserEvents();
                }
            }
        });
    }

    private void fetchUserEvents() {
        RealmQuery query = realm.where(UserEvent.class);
        UserEvent.fetchUserEvents(getActivity(), realm, Api.urlUserEventList(user.getId()), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
            }

            @Override
            public void fetchDidFail(Exception e) {
                if (realmResults.size() < 1) {
                    showMessage(R.string.server_error);
                }
            }
        });
    }

    @Override
    public void gotoDetailActivity(String id) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(HRDFConstants.KEY_EVENT_ID, id);
        intent.putExtra(HRDFConstants.KEY_EVENT_TYPE, type);
        startActivity(intent);

    }

    @Override
    public void performOperationOnEvent(EventOP op, String eventId) {
        Event event = Event.getEvent(eventId, realm);
        switch (op) {
            case SHARE_SOCIAL:
                Intent textShareIntent = new Intent(Intent.ACTION_SEND);
                textShareIntent.putExtra(Intent.EXTRA_TEXT, aboutus.getSocialMediaShareText() + " \n" + aboutus.getSocialMediaShareLink());
                // textShareIntent.putExtra(Intent.EXTRA_SUBJECT, aboutus.getSocialMediaShareText() + "\n" + aboutus.getSocialMediaSharePic());
                textShareIntent.setType("text/plain");
                //startActivity(Intent.createChooser(aboutus.createShareIntent(), "Share Image"));
                chooserIntent();
                break;
            case MARK_FAV:
                realm.beginTransaction();
                event.setFavourite(true);
                realm.commitTransaction();
                break;
            case UNMARK_FAV:
                realm.beginTransaction();
                event.setFavourite(false);
                realm.commitTransaction();
                break;
            case LAUNCH_ACTIVITY:
                Log.i(HRDFConstants.TAG, "Item clicked LAUNCH_ACTIVITY type=" + type + " Type " + eventId);
                if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_FEEDBACK)) {
                    event = Event.getEvent(eventId, realm);
                    sdkInstance.onStart(getActivity(), getString(R.string.app_name), 0, event.getSurveyId());
                    sdkInstance.startSMFeedbackActivityForResult(getActivity(), 0, event.getSurveyId());
                } else {
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    intent.putExtra(HRDFConstants.KEY_EVENT_ID, eventId);
                    intent.putExtra(HRDFConstants.KEY_EVENT_TYPE, type);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void showMessage(int message) {
        RelativeLayout messageLayout = (RelativeLayout) rootView.findViewById(R.id.error_layout);
        TextView messageView = (TextView) rootView.findViewById(R.id.label);
        messageView.setText(message);
        messageLayout.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        RelativeLayout messageLayout = (RelativeLayout) rootView.findViewById(R.id.error_layout);
        messageLayout.setVisibility(View.GONE);
    }

    private void chooserIntent() {
        // Drawable mDrawable = getResources().getDrawable(R.drawable.share_image, null);
        // Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, aboutus.getSocialMediaShareText() + " \n" + aboutus.getSocialMediaShareLink());
//        if(path != null) {
//            Uri uri = Uri.parse(path);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        }
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }
}

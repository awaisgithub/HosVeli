package com.od.hrdf.event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.Article;
import com.od.hrdf.BOs.Event;
import com.od.hrdf.BOs.User;
import com.od.hrdf.BOs.UserEvent;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.CallBack.StatusCallBack;
import com.od.hrdf.Payload.JSONPayloadManager;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.Utils.SectionsPagerAdapter;
import com.od.hrdf.abouts.AboutUs;
import com.od.hrdf.event.agenda.AgendsMainActivity;
import com.od.hrdf.event.exhibitor.ExhibitorListFragment;
import com.od.hrdf.event.floorplan.FloorPlanFragment;
import com.od.hrdf.event.speaker.SpeakerListFragment;
import com.od.hrdf.event.sponsor.SponsorListFragment;
import com.od.hrdf.landingtab.TabbarActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.hrdf.HRDFApplication.realm;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Realm realm;
    private Event event;
    private ViewPager mViewPager;
    private boolean isEventBooked;
    private boolean isEventFav;
    private User user;
    private AboutUs aboutUs;
    private ProgressDialog progressDialog;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        String eventId = getIntent().getStringExtra(HRDFConstants.KEY_EVENT_ID);
        realm = Realm.getDefaultInstance();
        event = Event.getEvent(eventId, realm);
        user = User.getCurrentUser(realm);
        aboutUs = AboutUs.getAboutUs(realm);
        if (event.getFavourite()) {
            isEventFav = true;
        } else {
            isEventFav = false;
        }

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
        MenuItem shareItem = menu.findItem(R.id.share_item);
        Drawable icon = shareItem.getIcon();
        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        MenuItem favItem = menu.findItem(R.id.fav_item);
        if (isEventFav) {
            favItem.setIcon(ContextCompat.getDrawable(this, R.drawable.bookmark_filled_white));
        } else {
            favItem.setIcon(ContextCompat.getDrawable(this, R.drawable.bookmark_white));
        }

        Drawable favIcon = favItem.getIcon();
        favIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(chooserIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.fav_item) {
            if (isEventFav) {
                isEventFav = false;
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.bookmark_white));
            } else {
                isEventFav = true;
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.bookmark_filled_white));
            }

            realm.beginTransaction();
            event.setFavourite(isEventFav);
            realm.commitTransaction();
            Drawable favIcon = item.getIcon();
            favIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
        return true;
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        mViewPager = (ViewPager) findViewById(R.id.event_detail_pager);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(HRDFConstants.LAUNCH_TYPE_KEY)) {
            String launchType = intent.getStringExtra(HRDFConstants.LAUNCH_TYPE_KEY);
            if (launchType.equalsIgnoreCase(HRDFConstants.LAUNCH_TYPE_GCM)) {
                String eventId = intent.getStringExtra(HRDFConstants.KEY_GCM_ITEM_ID);
                showProgressDialog(R.string.news_loading_detail);
                fetchEventById(eventId);
            } else {
                setInfo();
            }
        } else {
            setInfo();
        }
    }

    private void setInfo() {
        if (UserEvent.getUserEvent(realm, event.getId(), user.getId()) != null)
            isEventBooked = true;

        TextView dateTime = (TextView) findViewById(R.id.date_time);
        setDateTimeView(dateTime);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(event.getTitle());
        SimpleDraweeView headerImage = (SimpleDraweeView) findViewById(R.id.event_detail_header);
        if (event.getImage() != null) {
            String image = event.getImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            headerImage.setImageURI(uri);
            headerImage.setScaleType(ImageView.ScaleType.FIT_XY);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            headerImage.setController(controller);
        }

        final SegmentedGroup segmentedGroup = (SegmentedGroup) findViewById(R.id.event_detail_segment_control);
        segmentedGroup.setTintColor(ContextCompat.getColor(this, R.color.colorTabs), Color.WHITE);
        findViewById(R.id.event_detail_info).setOnClickListener(this);
        findViewById(R.id.event_detail_speaker).setOnClickListener(this);
        findViewById(R.id.event_detail_sponsor).setOnClickListener(this);
        findViewById(R.id.event_detail_exhibitor).setOnClickListener(this);
        findViewById(R.id.event_detail_floorplan).setOnClickListener(this);
        findViewById(R.id.button_register).setOnClickListener(this);
        findViewById(R.id.button_agenda).setOnClickListener(this);
        findViewById(R.id.button_map).setOnClickListener(this);

        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) segmentedGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String type = getIntent().getStringExtra(HRDFConstants.KEY_EVENT_TYPE);
        if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_UPCOMING)) {
            if (isEventBooked) {
                ((TextView) findViewById(R.id.button_register)).setText("Registered");
                ((TextView) findViewById(R.id.button_register)).setBackground(ContextCompat.getDrawable(this, R.drawable.rect_blue_button_register));
            } else {
                ((TextView) findViewById(R.id.button_register)).setText("Register");
            }
        } else if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_ARCHIVE)) {
            findViewById(R.id.layout_actions).setVisibility(View.GONE);
            findViewById(R.id.button_register).setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_MY_EVENTS)) {
            ((TextView) findViewById(R.id.button_register)).setText("Registered");
            ((TextView) findViewById(R.id.button_register)).setBackground(ContextCompat.getDrawable(this, R.drawable.rect_blue_button_register));
        } else if (type.equalsIgnoreCase(EventFragment.LIST_TYPE_FAV_EVENTS)) {
            if (isEventBooked) {
                ((TextView) findViewById(R.id.button_register)).setText("Registered");
                ((TextView) findViewById(R.id.button_register)).setBackground(ContextCompat.getDrawable(this, R.drawable.rect_blue_button_register));
            } else {
                ((TextView) findViewById(R.id.button_register)).setText("Register");
            }
        }

    }

    private void setDateTimeView(TextView tv) {
        String startDateTime = null;
        try {
            startDateTime = (String) DateFormat.format("EEE, MMM dd", event.getStartDate());
            SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
            Date timeObj = formatter.parse(event.getStartTime());
            String time = new SimpleDateFormat("hh:mm a").format(timeObj);
            time = time.replace(".", "");
            startDateTime = startDateTime + ", " + time;
        } catch (ParseException e) {
            e.printStackTrace();
            startDateTime = "";
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        tv.setText(startDateTime);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(EventInfoFragment.newInstance(event.getId()), "Event Info");
        adapter.addFragment(SpeakerListFragment.newInstance(event.getId()), "Speaker Info");
        adapter.addFragment(SponsorListFragment.newInstance(event.getId()), "Sponsors");
        adapter.addFragment(ExhibitorListFragment.newInstance(event.getId()), "Exhibitors");
        adapter.addFragment(FloorPlanFragment.newInstance(event.getId()), "Floor Plan");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();
        switch (viewId) {
            case R.id.event_detail_info:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.event_detail_speaker:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.event_detail_sponsor:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.event_detail_exhibitor:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.event_detail_floorplan:
                mViewPager.setCurrentItem(4);
                break;
            case R.id.button_agenda:
                Intent intent = new Intent(EventDetailActivity.this, AgendsMainActivity.class);
                intent.putExtra(HRDFConstants.KEY_EVENT_ID, event.getId());
                startActivity(intent);
                break;
            case R.id.button_map:
                Intent intentLocActivity = new Intent(EventDetailActivity.this, EventLocationActivity.class);
                if (!event.getLocationLatitude().isEmpty() && !event.getLocationLongitude().isEmpty()) {
                    double lat = Double.parseDouble(event.getLocationLatitude());
                    double lng = Double.parseDouble(event.getLocationLongitude());
                    intentLocActivity.putExtra("lat", lat);
                    intentLocActivity.putExtra("lng", lng);
                } else {
                }
                intentLocActivity.putExtra("address", event.getLocation());
                startActivity(intentLocActivity);
                break;
            case R.id.button_register:
                if (!isEventBooked)
                    bookEvent();
                break;
            default:
                break;
        }
    }

    private void bookEvent() {
        Event.bookEvent(user.getId(), event.getId(), Api.urlJogetCRUD(), new StatusCallBack() {
            @Override
            public void success(JSONObject response) {
                Log.i(HRDFConstants.TAG, "bookEvent success=" + response);
                if (response.has("status")) {
                    String status = response.optString("status");
                    if (status.equalsIgnoreCase("1")) {
                        ((TextView) findViewById(R.id.button_register)).setText("Registered");
                        ((TextView) findViewById(R.id.button_register)).setBackground(ContextCompat.getDrawable(EventDetailActivity.this, R.drawable.rect_blue_button_register));
                        realm.beginTransaction();
                        UserEvent userEvent = null;
                        if (UserEvent.getUserEvent(realm, event.getId()) != null) {
                            userEvent = UserEvent.getUserEvent(realm, event.getId());
                            userEvent.setUser(user.getId());
                            user.getEvents().add(userEvent);
                        } else {
                            userEvent = realm.createObject(UserEvent.class, event.getId());
                            userEvent.setUser(user.getId());
                            user.getEvents().add(userEvent);
                        }
                        realm.commitTransaction();
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void failure(String response) {
                Log.i(HRDFConstants.TAG, "bookEvent failure=" + response);
            }
        });
    }

    private void fetchEventById(final String eventId) {
        RealmQuery query = realm.where(Article.class);
        Event.fetchAllEvents(this, realm, Api.urlEventById(eventId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                hideProgressDialog();
                Event eventItem = Event.getEvent(eventId, realm);
                if (eventItem != null) {
                    event = eventItem;
                    setInfo();
                } else {
                    showMessage(R.string.server_error);
                }
            }

            @Override
            public void fetchDidFail(Exception e) {

                hideProgressDialog();
                showMessage(R.string.server_error);
            }
        });
    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {

        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

        }

        @Override
        public void onFailure(String id, Throwable throwable) {

        }
    };

    private void showMessage(int message) {
        RelativeLayout messageLayout = (RelativeLayout) findViewById(R.id.error_layout);
        TextView messageView = (TextView) findViewById(R.id.label);
        messageView.setText(message);
        messageLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
    }

    private void hideMessage() {
        RelativeLayout messageLayout = (RelativeLayout) findViewById(R.id.error_layout);
        messageLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
    }

    private void showProgressDialog(int message) {
        ((TextView) findViewById(R.id.button_register)).setVisibility(View.GONE);
        progressDialog.setMessage(getResources().getString(message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        ((TextView) findViewById(R.id.button_register)).setVisibility(View.VISIBLE);
        progressDialog.dismiss();
    }

    private Intent chooserIntent() {
        Drawable mDrawable = getResources().getDrawable(R.drawable.share_image, null);
        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image I want to share", null);
        Uri uri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, aboutUs.getSocialMediaShareText() + " \n" + aboutUs.getSocialMediaShareLink());
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        return shareIntent;
    }

}

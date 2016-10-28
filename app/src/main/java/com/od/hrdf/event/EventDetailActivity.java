package com.od.hrdf.event;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.od.hrdf.BOs.Event;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.Utils.SectionsPagerAdapter;
import com.od.hrdf.event.agenda.AgendsMainActivity;
import com.od.hrdf.event.exhibitor.ExhibitorListFragment;
import com.od.hrdf.event.floorplan.FloorPlanFragment;
import com.od.hrdf.event.speaker.SpeakerListFragment;
import com.od.hrdf.event.sponsor.SponsorListFragment;
import com.od.hrdf.landingtab.TabbarActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Realm realm;
    private Event event;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        String eventId = getIntent().getStringExtra(HRDFConstants.KEY_EVENT_ID);
        realm = Realm.getDefaultInstance();
        event = Event.getEvent(eventId, realm);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Events");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView dateTime = (TextView) findViewById(R.id.date_time);
        setDateTimeView(dateTime);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(event.getTitle());
        SimpleDraweeView headerImage = (SimpleDraweeView) findViewById(R.id.event_detail_header);
        if(event.getImage() != null) {
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
        findViewById(R.id.event_detail_info).setOnClickListener(this);
        findViewById(R.id.event_detail_speaker).setOnClickListener(this);
        findViewById(R.id.event_detail_sponsor).setOnClickListener(this);
        findViewById(R.id.event_detail_exhibitor).setOnClickListener(this);
        findViewById(R.id.event_detail_floorplan).setOnClickListener(this);

        findViewById(R.id.button_agenda).setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.event_detail_pager);
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton)segmentedGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setDateTimeView(TextView tv) {
        String startDateTime = null;

        try {
            startDateTime = (String) DateFormat.format("EEE, MMM dd", event.getStartDate());
            SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
            Date timeObj = formatter.parse(event.getStartTime());
            String time = new SimpleDateFormat("hh:mm a").format(timeObj);
            time = time.replace(".","");
            startDateTime = startDateTime+", "+time;
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
            default:
                break;
        }
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

}

package com.od.hrdf.event.speaker;

import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.od.hrdf.BOs.Speaker;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.Utils.SectionsPagerAdapter;
import com.od.hrdf.event.EventFragment;
import com.od.hrdf.event.EventListFragment;
import com.od.hrdf.landingtab.TabbarActivity;

import info.hoang8f.android.segmented.SegmentedGroup;

public class SpeakerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String speakerId;
    private Speaker speaker;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_detail);
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
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        speakerId = getIntent().getStringExtra(HRDFConstants.KEY_SPEAKER_ID);
        speaker = Speaker.getSpeaker(speakerId, HRDFApplication.realm);
        initViews();

    }

    private void initViews() {
        TextView name = (TextView) findViewById(R.id.name);
        TextView designation = (TextView) findViewById(R.id.designation);
        TextView overview = (TextView) findViewById(R.id.overview);
        SimpleDraweeView speakerPhoto = (SimpleDraweeView) findViewById(R.id.speaker_photo);
        name.setText(speaker.getName());
        designation.setText(speaker.getJobTitle());
        overview.setText(speaker.getOrganization());
        if(speaker.getImage() != null) {
            String image = speaker.getImage();
            image = image.replaceAll(" ", "%20");
            Uri uri = Uri.parse(image);
            speakerPhoto.setImageURI(uri);
            speakerPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(uri)
                    .build();
            speakerPhoto.setController(controller);
        }

        final SegmentedGroup segmentedGroup = (SegmentedGroup) findViewById(R.id.sp_detail_segment_control);
        segmentedGroup.setTintColor(ContextCompat.getColor(this, R.color.colorLightBlue), Color.WHITE);
        ((RadioButton) segmentedGroup.findViewById(R.id.sp_about)).setOnClickListener(this);
        ((RadioButton) segmentedGroup.findViewById(R.id.sp_topics)).setOnClickListener(this);
        ((RadioButton) segmentedGroup.findViewById(R.id.sp_uploads)).setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.sp_detail_pager);
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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(AboutSpeakerFragment.newInstance(speaker), "About Speaker");
        adapter.addFragment(TabbarActivity.PlaceholderFragment.newInstance(2), "THREE");
        adapter.addFragment(TabbarActivity.PlaceholderFragment.newInstance(3), "THREE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();
        switch (viewId) {
            case R.id.sp_about:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.sp_topics:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.sp_uploads:
                mViewPager.setCurrentItem(2);
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

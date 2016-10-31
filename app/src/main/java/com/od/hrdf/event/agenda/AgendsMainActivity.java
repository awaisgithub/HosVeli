package com.od.hrdf.event.agenda;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.od.hrdf.API.Api;
import com.od.hrdf.BOs.Agenda;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.Utils.SectionsPagerAdapter;
import com.od.hrdf.event.EventDetailActivity;
import com.od.hrdf.event.EventInfoFragment;
import com.od.hrdf.event.floorplan.FloorPlanFragment;
import com.od.hrdf.event.speaker.SpeakerListFragment;
import com.od.hrdf.event.sponsor.SponsorListFragment;
import com.od.hrdf.landingtab.TabbarActivity;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.od.hrdf.HRDFApplication.realm;

public class AgendsMainActivity extends AppCompatActivity implements View.OnClickListener{
    protected static final String AGENDA_DEC_SIX = "2016-12-06";
    protected static final String AGENDA_DEC_SEVEN = "2016-12-07";
    private String eventId;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agends_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Agenda");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        eventId = getIntent().getStringExtra(HRDFConstants.KEY_EVENT_ID);
        initViews();
        fetchAgenda();
    }

    private void initViews() {
        final SegmentedGroup segmentedGroup = (SegmentedGroup) findViewById(R.id.agenda_segment_control);
        segmentedGroup.setTintColor(ContextCompat.getColor(this, R.color.colorAgendaTabs), Color.BLACK);
        findViewById(R.id.date_six).setOnClickListener(this);
        findViewById(R.id.date_seven).setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.agends_pager);
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
        adapter.addFragment(AgendaListFragment.newInstance(AGENDA_DEC_SIX, eventId), "Agenda DEC 6");
        adapter.addFragment(AgendaListFragment.newInstance(AGENDA_DEC_SEVEN, eventId), "Agenda DEC 7");
        viewPager.setAdapter(adapter);
    }

    private void fetchAgenda() {
        RealmQuery query = realm.where(Agenda.class).equalTo("event", eventId);
        Agenda.fetchEventAgenda(this, realm, Api.urlEventAgenda(eventId), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
                Log.i(HRDFConstants.TAG, "!!!!RESULT LIST   YES!!!!= " + fetchedItems.toString());
            }

            @Override
            public void fetchDidFail(Exception e) {
                Log.i(HRDFConstants.TAG, "!!!!NO   ===   !!!!= " + e.toString());
            }
        });

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.date_six:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.date_seven:
                mViewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
}
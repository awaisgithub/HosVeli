package com.od.mma.landingtab;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.od.mma.API.Api;
import com.od.mma.BOs.Exhibitor;
import com.od.mma.BOs.Speaker;
import com.od.mma.BOs.Sponsor;
import com.od.mma.BOs.User;
import com.od.mma.CallBack.CheckCallBack;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.CallBack.StatusCallBack;
import com.od.mma.MMAApplication;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;
import com.od.mma.Utils.Util;
import com.od.mma.abouts.AboutMainFragment;
import com.od.mma.event.EventDetailActivity;
import com.od.mma.event.EventFragment;
import com.od.mma.gcm.MyGcmListenerService;
import com.od.mma.gcm.RegistrationIntentService;
import com.od.mma.loginregistration.LoginRegistrationActivity;
import com.od.mma.news.NewsDetailsActivity;
import com.od.mma.news.NewsFragment;
import com.od.mma.profile.ProfileFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

public class TabbarActivity extends AppCompatActivity implements TabFragActivityInterface {

    private ViewPager mViewPager;
    private Toolbar toolbar;
    Realm realm;
    User user;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TabLayout tabLayout;
    private int[] tabIconsNormal = {
            R.drawable.about,
            R.drawable.news,
            R.drawable.event,
            R.drawable.profile
    };
    private int[] tabIconsSelected = {
            R.drawable.about_selected,
            R.drawable.news_selected,
            R.drawable.event_selected,
            R.drawable.profile_selected
    };
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        realm = Realm.getDefaultInstance();
        user = User.getCurrentUser(realm);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setOffscreenPageLimit(4);
        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                assert textView != null;
                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsSelected[tab.getPosition()], 0, 0);
                //textView.setTextColor(ContextCompat.getColor(TabbarActivity.this, R.color.colorAccent));
                toolbarTitle.setText(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                assert textView != null;
                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsNormal[tab.getPosition()], 0, 0);
                //textView.setTextColor(ContextCompat.getColor(TabbarActivity.this, android.R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupTabIcons();
        fetchAllSpeakers();
        fetchAllSponsors();
        fetchAllExhibitors();

        try {
            ShortcutBadger.removeCountOrThrow(getApplicationContext());
        } catch (ShortcutBadgeException e) {
            e.printStackTrace();
        }
        MyGcmListenerService.count = 0;

        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(0);

        setUpGCMBroadCastReceiver();
        registerGCM();
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(MMAConstants.LAUNCH_TYPE_KEY)) {
            String launchType = intent.getStringExtra(MMAConstants.LAUNCH_TYPE_KEY);
            if (launchType.equalsIgnoreCase(MMAConstants.LAUNCH_TYPE_GCM)) {
                String from = intent.getStringExtra(MMAConstants.GCM_INTENT_KEY_FROM);
                if(from != null && from.equalsIgnoreCase(MMAConstants.GCM_FROM_ARTICLE)) {
                    String articleId = intent.getStringExtra(MMAConstants.KEY_GCM_ITEM_ID);
                    gotoNewsDetailActivity(articleId);
                } else if(from != null && from.equalsIgnoreCase(MMAConstants.GCM_FROM_UPCOMING_EVENT)) {
                    String eventId = intent.getStringExtra(MMAConstants.KEY_GCM_ITEM_ID);
                    String message = intent.getStringExtra(MMAConstants.GCM_INTENT_KEY_MESSAGE);
                    gotoEventDetailActivity(eventId, message);
                } else if(from != null && from.equalsIgnoreCase(MMAConstants.GCM_FROM_GENERAL)){
                    String message = intent.getStringExtra(MMAConstants.GCM_INTENT_KEY_MESSAGE);
                    normalPushNotificationDialog(message);
                }
            } else {

            }
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_tabbar, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(MMAConstants.GCM_BROADCAST_INTENT_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private void setUpGCMBroadCastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String gcmToken = intent.getStringExtra("token");
                Log.i(MMAConstants.TAG, "Token onReceive =" + gcmToken);
                if (gcmToken == null || gcmToken.length() < 1)
                    return;

                updateGCM(gcmToken);
            }
        };
    }

    private void updateGCM(final String token) {
        User.checkDuplicate(Api.urlGCMList(user.getId()), new CheckCallBack() {
            @Override
            public void checkDuplicateUser(boolean isExist) {
                Log.i(MMAConstants.TAG, "isExist =" + isExist);
                if (isExist) {
                    updateGCMOnServer(token);
                } else {
                    createGCMOnServer(token);
                }
            }

            @Override
            public void checkFail(Exception e) {

            }
        });
    }

    private void createGCMOnServer(String token) {

        User.addGCMId(user.getId(), token, Api.urlJogetCRUD(), new StatusCallBack() {
            @Override
            public void success(JSONObject response) {
                Log.i(MMAConstants.TAG, "createGCMOnServer success=" + response.toString());
            }

            @Override
            public void failure(String response) {
                Log.i(MMAConstants.TAG, "createGCMOnServer failure=" + response);
            }
        });
    }

    private void updateGCMOnServer(String token) {

        User.updateGCMId(user.getId(), token, Api.urlJogetCRUD(), new StatusCallBack() {
            @Override
            public void success(JSONObject response) {
                Log.i(MMAConstants.TAG, "updateGCMOnServer success=" + response.toString());
            }

            @Override
            public void failure(String response) {
                Log.i(MMAConstants.TAG, "updateGCMOnServer failure=" + response);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(AboutMainFragment.newInstance("", ""), "About Main");
        adapter.addFragment(NewsFragment.newInstance("", ""), "News Feed");
        adapter.addFragment(EventFragment.newInstance("", ""), "Events");
        adapter.addFragment(ProfileFragment.newInstance("", ""), "My Profile");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        String[] tabs = getResources().getStringArray(R.array.main_tabs);
        for (int i = 0; i < tabs.length; i++) {
            View parenLayout =  LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tabOne = (TextView) parenLayout.findViewById(R.id.tab);
            tabOne.setText(tabs[i]);
            if (i == 0) {
                toolbarTitle.setText(tabs[i]);
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsSelected[i], 0, 0);
            } else {
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsNormal[i], 0, 0);
            }
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    @Override
    public void onFragmentNav(Fragment fragment, Util.Navigate navigate) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.setSyncedLocal(false);
            }
        });

        Intent login = new Intent(this, LoginRegistrationActivity.class);
        startActivity(login);
        this.finish();
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.layout_error, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.label);
            return rootView;
        }
    }

    private void fetchAllSpeakers() {

        RealmQuery query = MMAApplication.realm.where(Speaker.class);
        Speaker.fetchAllSpeakers(this, MMAApplication.realm, Api.urlSpeakerList(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
    }

    private void fetchAllSponsors() {
        RealmQuery query = MMAApplication.realm.where(Sponsor.class);
        Sponsor.fetchAllSponsors(this, MMAApplication.realm, Api.urlAllSponsor(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
    }

    private void fetchAllExhibitors() {
        RealmQuery query = MMAApplication.realm.where(Exhibitor.class);
        Exhibitor.fetchExhibitor(this, MMAApplication.realm, Api.urlAllExhibitor(), query, new FetchCallBack() {
            @Override
            public void fetchDidSucceed(RealmResults fetchedItems) {
            }

            @Override
            public void fetchDidFail(Exception e) {
            }
        });
    }

    private void gotoNewsDetailActivity(String articleId) {
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra(MMAConstants.LAUNCH_TYPE_KEY, MMAConstants.LAUNCH_TYPE_GCM);
        intent.putExtra(MMAConstants.KEY_GCM_ITEM_ID, articleId);
        startActivity(intent);
    }

    private void gotoEventDetailActivity(String eventId, String message) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra(MMAConstants.LAUNCH_TYPE_KEY, MMAConstants.LAUNCH_TYPE_GCM);
        intent.putExtra(MMAConstants.KEY_EVENT_TYPE, EventFragment.LIST_TYPE_UPCOMING);
        intent.putExtra(MMAConstants.GCM_INTENT_KEY_MESSAGE, message);
        intent.putExtra(MMAConstants.KEY_GCM_ITEM_ID, eventId);
        startActivity(intent);
    }

    private void registerGCM() {
        if (checkPlayServices()) {
            Intent gcmServiceIntent = new Intent(this, RegistrationIntentService.class);
            startService(gcmServiceIntent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int resultCode = gApi.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (gApi.isUserResolvableError(resultCode)) {
                gApi.getErrorDialog(this, resultCode, MMAConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }

            return false;
        }
        return true;
    }

    private void normalPushNotificationDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ThemeDialogCustom);
        //builder.setTitle(R.string.dialog_congrats_title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

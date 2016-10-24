package com.od.hrdf.landingtab;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.od.hrdf.R;
import com.od.hrdf.Utils.Util;
import com.od.hrdf.abouts.AboutUsFragment;
import com.od.hrdf.news.NewsFragment;
import com.od.hrdf.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class TabbarActivity extends AppCompatActivity implements TabFragActivityInterface{

    private ViewPager mViewPager;
    private Toolbar toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(4);
        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                assert textView != null;
                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsSelected[tab.getPosition()], 0, 0);
                textView.setTextColor(ContextCompat.getColor(TabbarActivity.this, R.color.colorAccent));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                assert textView != null;
                textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsNormal[tab.getPosition()], 0, 0);
                textView.setTextColor(ContextCompat.getColor(TabbarActivity.this, android.R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupTabIcons();
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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(AboutUsFragment.newInstance("", ""), "About Us");
        adapter.addFragment(NewsFragment.newInstance("",""), "News");
        adapter.addFragment(PlaceholderFragment.newInstance(2), "THREE");
        adapter.addFragment(ProfileFragment.newInstance("", ""), "Profile");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        String[] tabs = getResources().getStringArray(R.array.main_tabs);
        for (int i = 0; i < tabs.length; i++) {
            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabOne.setText(tabs[i]);
            if(i == 0) {
                tabOne.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsSelected[i], 0, 0);
            } else {
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIconsNormal[i], 0, 0);
            }
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    @Override
    public void onFragmentNav(Fragment fragment, Util.Navigate navigate) {

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
            View rootView = inflater.inflate(R.layout.fragment_tabbar, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
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

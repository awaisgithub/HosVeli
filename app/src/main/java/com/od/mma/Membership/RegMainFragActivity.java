package com.od.mma.Membership;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.od.mma.API.Api;
import com.od.mma.R;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by awais on 27/12/2016.
 */

public class RegMainFragActivity extends FragmentActivity implements FragInterface {
    Toolbar toolbar;
    TextView activityTitle;
    Membership reg;
    ArrayList<Fragment> pagerFragments = new ArrayList<>();
    TextView toolbar_title;


    @Override
    public void onBackPressed() {
        PagerViewPager.setPos(0);
        super.onBackPressed();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_main);
        toolbar_title = (TextView) findViewById(R.id.activity_title);

        populatePagerFragments();
        PagerViewPager.realm = Realm.getDefaultInstance();
        PagerViewPager.membership = Membership.getCurrentRegistration(PagerViewPager.realm);

        if (PagerViewPager.membership == null) {
            PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    reg = PagerViewPager.realm.createObject(Membership.class);
                    reg.setSyncedLocal(true);
                    PagerViewPager.setMembership(reg);
                }
            });
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        activityTitle = (TextView) findViewById(R.id.activity_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), pagerFragments));
        PagerViewPager.setPager(pager);
        tabLayout.setupWithViewPager(pager, true);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }


            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbar_title.setText("Membership Category");
                        PagerViewPager.setPos(0);
                        break;
                    case 1:
                        toolbar_title.setText("Personal Info");
                        CategoryFrag categoryFrag = (CategoryFrag) pagerFragments.get(0);
                        categoryFrag.validation();
                        PagerViewPager.setPos(1);
                        break;
                    case 2:
                        toolbar_title.setText("Identity Info");
                        RegNameFrag regNameFrag = (RegNameFrag) pagerFragments.get(1);
                        regNameFrag.validation();
                        PagerViewPager.setPos(2);
                        break;
                    case 3:
                        toolbar_title.setText("Working Address Info");
                        RegIdentityFrag identityFrag = (RegIdentityFrag) pagerFragments.get(2);
                        identityFrag.validation();
                        PagerViewPager.setPos(3);
                        break;
                    case 4:
                        toolbar_title.setText("Residential Address Info");
                        AddressWorkingFrag addressWorkingFrag = (AddressWorkingFrag) pagerFragments.get(3);
                        addressWorkingFrag.validation();
                        PagerViewPager.setPos(4);
                        break;
                    case 5:
                        toolbar_title.setText("Spouse Info");
                        AddressResidentialFrag addressResidentialFrag = (AddressResidentialFrag) pagerFragments.get(4);
                        addressResidentialFrag.validation();
                        PagerViewPager.setPos(5);
                        break;
                    case 6:
                        toolbar_title.setText("Basic Degree Info");
                        SpouseFrag spouseFrag = (SpouseFrag) pagerFragments.get(5);
                        spouseFrag.validation();
                        PagerViewPager.setPos(6);
                        break;
                    case 7:
                        toolbar_title.setText("Post Graduate Degree Info");
                        BachelorDegreeFrag bachelorDegreeFrag = (BachelorDegreeFrag) pagerFragments.get(6);
                        bachelorDegreeFrag.validation();
                        PagerViewPager.setPos(7);
                        break;
                    case 8:
                        if (PagerViewPager.membership.getMain_category().equals("Student"))
                            toolbar_title.setText("University Info");
                        else
                            toolbar_title.setText("MMC Registration Info");
                        PagerViewPager.setPos(8);
                        break;
                    case 9:
                        toolbar_title.setText("Employment Info");
                        MMCInfoFrag mmcInfoFrag = (MMCInfoFrag) pagerFragments.get(8);
                        mmcInfoFrag.validation();
                        PagerViewPager.setPos(9);
                        break;
                    case 10:
//                        toolbar_title.setText("Payment Method");
                        toolbar_title.setText("Application Status Details");
                        EmploymentInfoFrag employmentInfoFrag = (EmploymentInfoFrag) pagerFragments.get(9);
                        employmentInfoFrag.validation();
                        PagerViewPager.setPos(10);
                        break;
//                    case 11:
//                        toolbar_title.setText("Payment Details");
//                        PaymentFrag PaymentFrag = (PaymentFrag) pagerFragments.get(10);
//                        PaymentFrag.validation();
//                        PagerViewPager.setPos(11);
//                        break;
//                    case 12:
//                        toolbar_title.setText("Application Status Details");
//                        PaymentDetailsFrag PaymentDetailsFrag = (PaymentDetailsFrag) pagerFragments.get(11);
//                        PaymentDetailsFrag.validation();
//                        PagerViewPager.setPos(12);
//                        break;
                    default:
                        PagerViewPager.setPos(0);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    private void populatePagerFragments() {
        pagerFragments.add(CategoryFrag.newInstance("CategoryFrag, Instance 1"));
        pagerFragments.add(RegNameFrag.newInstance("RegNameFrag, Instance 1"));
        pagerFragments.add(RegIdentityFrag.newInstance("RegIdentityFrag, Instance 1"));
        pagerFragments.add(AddressWorkingFrag.newInstance("AddressWorkingFrag, Instance 1"));
        pagerFragments.add(AddressResidentialFrag.newInstance("AddressResidentialFrag, Instance 1"));
        pagerFragments.add(SpouseFrag.newInstance("SpouseFrag, Instance 1"));
        pagerFragments.add(BachelorDegreeFrag.newInstance("BachelorDegreeFrag, Instance 1"));
        pagerFragments.add(PostgradDegreeFrag.newInstance("PostgradDegreeFrag, Instance 1"));
        pagerFragments.add(MMCInfoFrag.newInstance("MMCInfoFrag, Instance 1"));
        pagerFragments.add(EmploymentInfoFrag.newInstance("EmploymentInfoFrag, Instance 1"));
     //   pagerFragments.add(PaymentFrag.newInstance("PaymentFrag, Instance 1"));
     //   pagerFragments.add(PaymentDetailsFrag.newInstance("PaymentDetailsFrag, Instance 1"));
        pagerFragments.add(ApplicationStatusFrag.newInstance("ApplicationStatusFrag, Instance 1"));
    }

    @Override
    public void NavigateTo(int position) {
        switch (position) {
            case 0:
                PagerViewPager.getPager().setCurrentItem(0, true);
                break;
            case 1:
                PagerViewPager.getPager().setCurrentItem(1, true);
                break;
            case 2:
                PagerViewPager.getPager().setCurrentItem(2, true);
                break;
            case 3:
                PagerViewPager.getPager().setCurrentItem(3, true);
                break;
            case 4:
                PagerViewPager.getPager().setCurrentItem(4, true);
                break;
            case 5:
                PagerViewPager.getPager().setCurrentItem(5, true);
                break;
            case 6:
                PagerViewPager.getPager().setCurrentItem(6, true);
                break;
            case 7:
                PagerViewPager.getPager().setCurrentItem(7, true);
                break;
            case 8:
                PagerViewPager.getPager().setCurrentItem(8, true);
                break;
            case 9:
                PagerViewPager.getPager().setCurrentItem(9, true);
                break;
            case 10:
                PagerViewPager.getPager().setCurrentItem(10, true);
                break;
//            case 11:
//                PagerViewPager.getPager().setCurrentItem(11, true);
//                break;
//            case 12:
//                PagerViewPager.getPager().setCurrentItem(12, true);
//                break;
            default:
                PagerViewPager.setPos(0);
        }
    }

    @Override
    public void onFragmentNav(MembershipToNav nav) {
        switch (nav) {
            case NEXT:
                PagerViewPager.getPager().setCurrentItem(PagerViewPager.getPos() + 1);
                break;
            case BACK:
                PagerViewPager.getPager().setCurrentItem(PagerViewPager.getPos() - 1);
                break;
        }
    }
}

class MyPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> pagerFragments;

    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> pagerFragments) {
        super(fm);
        this.pagerFragments = pagerFragments;
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return pagerFragments.get(0);
            case 1:
                return pagerFragments.get(1);
            case 2:
                return pagerFragments.get(2);
            case 3:
                return pagerFragments.get(3);
            case 4:
                return pagerFragments.get(4);
            case 5:
                return pagerFragments.get(5);
            case 6:
                return pagerFragments.get(6);
            case 7:
                return pagerFragments.get(7);
            case 8:
                return pagerFragments.get(8);
            case 9:
                return pagerFragments.get(9);
            case 10:
                return pagerFragments.get(10);
//            case 11:
//                return pagerFragments.get(11);
//            case 12:
//                return pagerFragments.get(12);
            default:
                return pagerFragments.get(0);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 11;
    }
}


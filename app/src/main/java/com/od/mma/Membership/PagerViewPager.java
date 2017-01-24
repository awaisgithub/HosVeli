package com.od.mma.Membership;

import android.support.v4.view.ViewPager;

import io.realm.Realm;

/**
 * Created by awais on 30/12/2016.
 */

public class PagerViewPager {
    static public ViewPager pager;
    static public int pos;

    public static ViewPager getPager() {
        return pager;
    }

    public static void setPager(ViewPager pager) {
        PagerViewPager.pager = pager;
    }

    public static int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        PagerViewPager.pos = pos;
    }
}

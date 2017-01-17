package com.od.mma.Membership;

import android.support.v4.view.ViewPager;

import io.realm.Realm;

/**
 * Created by awais on 30/12/2016.
 */

public class PagerViewPager {
    static public ViewPager pager;
    static public int pos;
    static public String category = "";
    static public Realm realm;
    static public Membership membership;

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

    public static String getCategory() {
        return category;
    }

    public static void setCategory(String category) {
        PagerViewPager.category = category;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public static Membership getMembership() {
        return membership;
    }

    public static void setMembership(Membership membership) {
        PagerViewPager.membership = membership;
    }
}

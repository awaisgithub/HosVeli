package com.od.mma.BOs;

/**
 * Created by Awais on 10/21/2016.
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SessionItem extends RealmObject {
    @PrimaryKey
    private String id;

    private String title;
    private String venue;
    private String moreInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getMoreinfo() {
        return moreInfo;
    }

    public void setMoreinfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

}


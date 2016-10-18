package com.od.hrdf.BOs;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Awais on 10/10/2016.
 */

public class NewsItem extends RealmObject {
    @PrimaryKey
    private String id;

    private String title;
    private String subTitle;
    private String contents;
    private String summaryImage;
    private String bannerImage;
    private String publishDate;

    public NewsItem() {
    }

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSummaryImage() {
        return summaryImage;
    }

    public void setSummaryImage(String summaryImage) {
        this.summaryImage = summaryImage;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    //METHODS

    public static NewsItem getNewsItem(String id, Realm realm) {
        return realm.where(NewsItem.class).equalTo("id", id)
                .findFirst();
    }

    public static RealmResults<NewsItem> getNewsItemsController(RealmResults delegate, Realm realm) {
        return realm.where(NewsItem.class)
                .findAll().sort("publishDate", Sort.DESCENDING);
    }
}

package com.od.mma.BOs;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.MMAApplication;
import com.od.mma.Utils.MMAConstants;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Awais on 10/18/2016.
 */

public class Organization extends RealmObject {
    @PrimaryKey
    private String id;

    private String address;
    private String phone;
    private String fax;
    private String email;
    private String website;
    private String bannerImage;
    private String aboutUs;
    private String facebookLink;
    private String twitterLink;
    private String instagramLink;
    private String linkedInLink;
    private String youtubeLink;
    private String socialMediaShareLink;
    private String socialMediaShareText;
    private String socialMediaSharePic;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public String getLinkedInLink() {
        return linkedInLink;
    }

    public void setLinkedInLink(String linkedInLink) {
        this.linkedInLink = linkedInLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getSocialMediaShareLink() {
        return socialMediaShareLink;
    }

    public void setSocialMediaShareLink(String socialMediaShareLink) {
        this.socialMediaShareLink = socialMediaShareLink;
    }

    public String getSocialMediaShareText() {
        return socialMediaShareText;
    }

    public void setSocialMediaShareText(String socialMediaShareText) {
        this.socialMediaShareText = socialMediaShareText;
    }

    public String getSocialMediaSharePic() {
        return socialMediaSharePic;
    }

    public void setSocialMediaSharePic(String socialMediaSharePic) {
        this.socialMediaSharePic = socialMediaSharePic;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public static Organization getOrgnizationInfo(Realm realm) {
        return realm.where(Organization.class).findFirst();
    }

    public static void fetchAboutHRDF(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(MMAConstants.TAG, response.toString());
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    realm.beginTransaction();
                                    realm.createOrUpdateAllFromJson(Organization.class, response);
                                    RealmResults aboutHRDF = query.findAll();
                                    realm.commitTransaction();
                                    callBack.fetchDidSucceed(aboutHRDF);
                                } catch (Exception e) {
                                    Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
                                    callBack.fetchDidFail(e);
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        MMAApplication.getInstance().addToRequestQueue(req);
    }
}

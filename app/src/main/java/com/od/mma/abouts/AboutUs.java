package com.od.mma.abouts;

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
 * Created by Awais on 10/21/2016.
 */

public class AboutUs extends RealmObject {
    @PrimaryKey
    private String id;

    private String phone;
    private String dateModified;
    private String fax;
    private String youtubeLink;
    private String website;
    private String socialMediaSharePic;
    private String aboutUs;
    private String facebookLink;
    private String email;
    private String address;
    private String socialMediaShareLink;
    private String twitterLink;
    private String socialMediaShareText;
    private String dateCreated;
    private String aboutUsBanner;
    private String instagramLink;
    private String linkedinLink;

    public static AboutUs getAboutUs(Realm realm) {
        return realm.where(AboutUs.class).findFirst();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSocialMediaSharePic() {
        return socialMediaSharePic;
    }

    public void setSocialMediaSharePic(String socialMediaSharePic) {
        this.socialMediaSharePic = socialMediaSharePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSocialMediaShareLink() {
        return socialMediaShareLink;
    }

    public void setSocialMediaShareLink(String socialMediaShareLink) {
        this.socialMediaShareLink = socialMediaShareLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getSocialMediaShareText() {
        return socialMediaShareText;
    }

    public void setSocialMediaShareText(String socialMediaShareText) {
        this.socialMediaShareText = socialMediaShareText;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAboutUsBanner() {
        return aboutUsBanner;
    }

    public void setAboutUsBanner(String aboutUsBanner) {
        this.aboutUsBanner = aboutUsBanner;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getLinkedinLink() {
        return linkedinLink;
    }

    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }

    public static void fetchAboutUs(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchAboutUs = " + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {

                        try {
                            realm.beginTransaction();
                            realm.createOrUpdateAllFromJson(AboutUs.class, response);
                            RealmResults about = query.findAll();
                            realm.commitTransaction();
                            callBack.fetchDidSucceed(about);
                        } catch (Exception e) {
                            Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
                            callBack.fetchDidFail(e);
                        }
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

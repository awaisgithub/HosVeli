package com.od.hrdf.abouts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.od.hrdf.BOs.User;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

import static com.od.hrdf.HRDFApplication.context;
import static com.od.hrdf.HRDFApplication.realm;

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
        Log.i(HRDFConstants.TAG, "fetchAboutUs = " + url);
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
                            Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
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
        HRDFApplication.getInstance().addToRequestQueue(req);
    }
}

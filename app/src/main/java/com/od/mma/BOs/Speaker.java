package com.od.mma.BOs;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.CallBack.StatusCallBack;
import com.od.mma.MMAApplication;
import com.od.mma.Payload.JSONPayloadManager;
import com.od.mma.Utils.MMAConstants;
import com.od.mma.event.speaker.SpeakerRateDialogFrag;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Awais on 10/10/2016.
 */

public class Speaker extends RealmObject {
    @PrimaryKey
    private String id;

    private String sector;
    private String hrdf_staff;
    private String dateModified;
    private String state;
    private String address1;
    private String  hrdf_mycoid;
    private String address2;
    private String race;
    private String city;
    private String organization;
    private String speakerEmail;
    private String name;
    private String age;
    private String  role;
    private String  gender;
    private String  industry;
    private String  attendedEvents;
    private String  jobTitle;
    private String  aboutSpeaker;
    private String  image;
    private String  postcode;
    private String  nationality;
    private String  officeNumber;
    private String  dateCreated;
    private String  salutation;
    private String  mobileNumber;
    private String  hrdf_status;
    private float rating;
    private boolean isRated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getHrdf_staff() {
        return hrdf_staff;
    }

    public void setHrdf_staff(String hrdf_staff) {
        this.hrdf_staff = hrdf_staff;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getHrdf_mycoid() {
        return hrdf_mycoid;
    }

    public void setHrdf_mycoid(String hrdf_mycoid) {
        this.hrdf_mycoid = hrdf_mycoid;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSpeakerEmail() {
        return speakerEmail;
    }

    public void setSpeakerEmail(String speakerEmail) {
        this.speakerEmail = speakerEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(String attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getAboutSpeaker() {
        return aboutSpeaker;
    }

    public void setAboutSpeaker(String aboutSpeaker) {
        this.aboutSpeaker = aboutSpeaker;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getHrdf_status() {
        return hrdf_status;
    }

    public void setHrdf_status(String hrdf_status) {
        this.hrdf_status = hrdf_status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    //METHODS

    public static Speaker getSpeaker(String id, Realm realm) {
        return realm.where(Speaker.class).equalTo("id", id)
                .findFirst();
    }

    public static RealmResults<Speaker> getSpeakerResultsController(Realm realm) {
        return realm.where(Speaker.class)
                .findAll().sort("name", Sort.DESCENDING);
    }

    public static void fetchAllSpeakers(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(MMAConstants.TAG, response.toString());
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
                                Log.i(MMAConstants.TAG, "In Run()");
                                try {
                                    realm.beginTransaction();
                                    realm.createOrUpdateAllFromJson(Speaker.class, response);
                                    RealmResults speakers = query.findAll();
                                    realm.commitTransaction();
                                    Log.i(MMAConstants.TAG, "In Run()-TransactionCommit");
                                    callBack.fetchDidSucceed(speakers);
                                } catch (Exception e) {
                                    Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
                                    callBack.fetchDidFail(e);
                                }
//                            }
//                        });
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

    public static void rateSpeaker(String url, SpeakerRateDialogFrag.SpeakerRatingBO ratingBO, final StatusCallBack callBack) {
        Log.i(MMAConstants.TAG, "performUserRegistration =" + url);
        JSONObject jsonObject = JSONPayloadManager.getInstance().getSpeakerRatingPayload(ratingBO);
        JsonObjectRequest rsvpRegistration = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.success(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.failure(error.toString());

            }
        });
        MMAApplication.getInstance().addToRequestQueue(rsvpRegistration);
    }
}
package com.od.hrdf.BOs;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.Utils.HRDFConstants;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Awais on 10/10/2016.
 */

public class Speaker extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private byte[] imageData;

    private Long speaker_age;
    private Long speaker_hrdf_staff;
    //
    private String cpeaker_sector;
    private String speaker_company;
    private String speaker_contact_number;
    private String speaker_office_contact;
    private String speaker_designation;
    private String speaker_email;
    private String speaker_employment_status;
    private String speaker_gender;
    private String speaker_hrdf_mycoid;
    private String speaker_hrdf_status;
    private String speaker_industry;
    private String speaker_name;
    private String speaker_nationality;
    private String speaker_race;
    private String speaker_role;
    private String speaker_salutation;
    private String speaker_photo;
    private String speaker_about;
    private String event;
    private AddressD address;
    private long rating;
    private long sequence;
    private boolean isImagePresent;
    private RealmList<SpeakerTopic> topics;

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getSpeaker_company() {
        return speaker_company;
    }

    public void setSpeaker_company(String speaker_company) {
        this.speaker_company = speaker_company;
    }

    public String getSpeaker_contact_number() {
        return speaker_contact_number;
    }

    public void setSpeaker_contact_number(String speaker_contact_number) {
        this.speaker_contact_number = speaker_contact_number;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setSpeaker_age(Long speaker_age) {
        this.speaker_age = speaker_age;
    }

    public void setSpeaker_hrdf_staff(Long speaker_hrdf_staff) {
        this.speaker_hrdf_staff = speaker_hrdf_staff;
    }

    public String getCpeaker_sector() {
        return cpeaker_sector;
    }

    public void setCpeaker_sector(String cpeaker_sector) {
        this.cpeaker_sector = cpeaker_sector;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public boolean isImagePresent() {
        return isImagePresent;
    }

    public void setImagePresent(boolean imagePresent) {
        isImagePresent = imagePresent;
    }

    public String getSpeaker_office_contact() {
        return speaker_office_contact;
    }

    public void setSpeaker_office_contact(String speaker_office_contact) {
        this.speaker_office_contact = speaker_office_contact;
    }

    public String getSpeaker_designation() {
        return speaker_designation;
    }

    public void setSpeaker_designation(String speaker_designation) {
        this.speaker_designation = speaker_designation;
    }

    public String getSpeaker_email() {
        return speaker_email;
    }

    public void setSpeaker_email(String speaker_email) {
        this.speaker_email = speaker_email;
    }

    public String getSpeaker_employment_status() {
        return speaker_employment_status;
    }

    public void setSpeaker_employment_status(String speaker_employment_status) {
        this.speaker_employment_status = speaker_employment_status;
    }

    public Long getSpeaker_age() {
        return speaker_age;
    }

    public Long getSpeaker_hrdf_staff() {
        return speaker_hrdf_staff;
    }

    public RealmList<SpeakerTopic> getTopics() {
        return topics;
    }

    public void setTopics(RealmList<SpeakerTopic> topics) {
        this.topics = topics;
    }

    public String getSpeaker_gender() {
        return speaker_gender;
    }

    public void setSpeaker_gender(String speaker_gender) {
        this.speaker_gender = speaker_gender;
    }

    public String getSpeaker_hrdf_mycoid() {
        return speaker_hrdf_mycoid;
    }

    public void setSpeaker_hrdf_mycoid(String speaker_hrdf_mycoid) {
        this.speaker_hrdf_mycoid = speaker_hrdf_mycoid;
    }


    public String getSpeaker_hrdf_status() {
        return speaker_hrdf_status;
    }

    public void setSpeaker_hrdf_status(String speaker_hrdf_status) {
        this.speaker_hrdf_status = speaker_hrdf_status;
    }

    public String getSpeaker_industry() {
        return speaker_industry;
    }

    public void setSpeaker_industry(String speaker_industry) {
        this.speaker_industry = speaker_industry;
    }

    public String getSpeaker_name() {
        return speaker_name;
    }

    public void setSpeaker_name(String speaker_name) {
        this.speaker_name = speaker_name;
    }

    public String getSpeaker_nationality() {
        return speaker_nationality;
    }

    public void setSpeaker_nationality(String speaker_nationality) {
        this.speaker_nationality = speaker_nationality;
    }

    public String getSpeaker_race() {
        return speaker_race;
    }

    public void setSpeaker_race(String speaker_race) {
        this.speaker_race = speaker_race;
    }

    public String getSpeaker_role() {
        return speaker_role;
    }

    public void setSpeaker_role(String speaker_role) {
        this.speaker_role = speaker_role;
    }

    public String getSpeaker_salutation() {
        return speaker_salutation;
    }

    public void setSpeaker_salutation(String speaker_salutation) {
        this.speaker_salutation = speaker_salutation;
    }

    public String getSpeaker_photo() {
        return speaker_photo;
    }

    public void setSpeaker_photo(String speaker_photo) {
        this.speaker_photo = speaker_photo;
    }

    public String getSpeaker_about() {
        return speaker_about;
    }

    public void setSpeaker_about(String speaker_about) {
        this.speaker_about = speaker_about;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public AddressD getAddress() {
        return address;
    }

    public void setAddress(AddressD address) {
        this.address = address;
    }

    //METHODS

    public static Speaker getSpeaker(String id, Realm realm) {
        return realm.where(Speaker.class).equalTo("id", id)
                .findFirst();
    }

    public static RealmResults<Speaker> getSpeakerResultsController(RealmResults delegate, Realm realm) {
        return realm.where(Speaker.class)
                .findAll().sort("speaker_name", Sort.DESCENDING);
    }

    public static RealmResults<Speaker> getEventSpeakerResultsController(RealmResults delegate, String eventId, Realm realm) {
        return realm.where(Speaker.class).contains("events.id", eventId)
                .findAll().sort("speaker_name", Sort.DESCENDING);
    }

    public static void fetchAllSpeakers(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(HRDFConstants.TAG, response.toString());
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(HRDFConstants.TAG, "In Run()");
                                try {
                                    realm.beginTransaction();
                                    Log.i(HRDFConstants.TAG, "In Run()-TransactionBegun");
                                    realm.createOrUpdateAllFromJson(Speaker.class, response);
                                    Log.i(HRDFConstants.TAG, "In Run()-CreateFromJSON");
                                    RealmResults speakers = query.findAll();
                                    realm.commitTransaction();
                                    Log.i(HRDFConstants.TAG, "In Run()-TransactionCommit");
                                    callBack.fetchDidSucceed(speakers);
                                } catch (Exception e) {
                                    Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
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
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

    public static void fetchEventSpeakers(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(HRDFConstants.TAG, response.toString());
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    realm.beginTransaction();
                                    realm.createOrUpdateAllFromJson(Speaker.class, response);
                                    RealmResults eventSpeakers = query.findAll();
                                    realm.commitTransaction();
                                    callBack.fetchDidSucceed(eventSpeakers);
                                } catch (Exception e) {
                                    Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
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
                        Log.i(HRDFConstants.TAG, "OnErrorRun()");
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

}
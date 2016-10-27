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
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MuhammadMahmoor on 10/27/16.
 */

public class EventSponsor extends RealmObject {

    @PrimaryKey
    private String id;

    @Index
    private String sponsorlevelseq;

    private String dateModified;
    private String sponsorLevel;
    private String remarks;
    private String datecreated;
    private String sponsor;
    private String sponsorlevelname;
    private String sponsorName;
    private String event;
    private String eventtitle;

    public String getSponsorlevelseq() {
        return sponsorlevelseq;
    }

    public void setSponsorlevelseq(String sponsorlevelseq) {
        this.sponsorlevelseq = sponsorlevelseq;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getSponsorLevel() {
        return sponsorLevel;
    }

    public void setSponsorLevel(String sponsorLevel) {
        this.sponsorLevel = sponsorLevel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSponsorlevelname() {
        return sponsorlevelname;
    }

    public void setSponsorlevelname(String sponsorlevelname) {
        this.sponsorlevelname = sponsorlevelname;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventtitle() {
        return eventtitle;
    }

    public void setEventtitle(String eventtitle) {
        this.eventtitle = eventtitle;
    }

    public static RealmList<EventSponsor> getEventSponsorList(Realm realm, String eventId) {
        RealmList<EventSponsor> sponsors = new RealmList<>();

        RealmResults realmResults = realm.where(EventSponsor.class).equalTo("event", eventId).findAll();
        for(int i=0; i<realmResults.size(); i++) {
            sponsors.add((EventSponsor) realmResults.get(i));
        }
        return sponsors;
    }

    public static RealmResults<EventSponsor> getDistinctByLevelSeq(Realm realm, String eventId) {
        return realm.where(EventSponsor.class).equalTo("event", eventId).findAll().distinct("sponsorlevelseq");
    }

    public static RealmResults<EventSponsor> getSponsorByLevelName(Realm realm, String sponsorlevelname) {
        return realm.where(EventSponsor.class).equalTo("sponsorlevelname", sponsorlevelname).findAll();
    }

    public static void fetchEventSponsors(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(HRDFConstants.TAG, "fetchEventSponsors ="+url);
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
                                    realm.createOrUpdateAllFromJson(EventSponsor.class, response);
                                    RealmResults eventSponsors = query.findAll();
                                    realm.commitTransaction();
                                    callBack.fetchDidSucceed(eventSponsors);
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

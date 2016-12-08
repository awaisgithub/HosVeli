package com.od.mma.BOs;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.MMAApplication;
import com.od.mma.Utils.MMAConstants;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
    private String sponsorWebsite;
    boolean isObsolete;

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

    public String getSponsorWebsite() {
        return sponsorWebsite;
    }

    public void setSponsorWebsite(String sponsorWebsite) {
        this.sponsorWebsite = sponsorWebsite;
    }

    public boolean isObsolete() {
        return isObsolete;
    }

    public void setObsolete(boolean obsolete) {
        isObsolete = obsolete;
    }

    public static RealmList<EventSponsor> getEventSponsorList(Realm realm, String eventId) {
        RealmList<EventSponsor> sponsors = new RealmList<>();

        RealmResults realmResults = realm.where(EventSponsor.class).equalTo("event", eventId).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            sponsors.add((EventSponsor) realmResults.get(i));
        }
        return sponsors;
    }

    public static EventSponsor getEventSponsor(Realm realm, String eventId) {
        return realm.where(EventSponsor.class).equalTo("id", eventId).findFirst();
    }

    public static RealmResults<EventSponsor> getDistinctByLevelSeq(Realm realm, String eventId) {
        return realm.where(EventSponsor.class).equalTo("event", eventId).findAll().distinct("sponsorlevelseq");
    }

    public static RealmResults<EventSponsor> getSponsorByLevelName(Realm realm, String sponsorlevelname) {
        return realm.where(EventSponsor.class).equalTo("sponsorlevelname", sponsorlevelname).findAll();
    }

    public static void fetchEventSponsors(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchEventSponsors =" + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(MMAConstants.TAG, response.toString());
                        try {
                            realm.beginTransaction();
                            Gson gson = new GsonBuilder().create();
                            Type collectionType = new TypeToken<ArrayList<EventSponsor>>() {
                            }.getType();
                            ArrayList<EventSponsor> sponsors = gson.fromJson(response.toString(), collectionType);
                            if (sponsors.size() > 0)
                                makeAllObsolete(realm);

                            for (EventSponsor sponsor : sponsors) {
                                EventSponsor localSponsor = EventSponsor.getEventSponsor(realm, sponsor.getId());
                                if (localSponsor != null) {
                                    sponsor.setObsolete(false);
                                } else {
                                }
                                realm.copyToRealmOrUpdate(sponsor);
                            }
                            deleteAllObsolete(realm);
                            realm.commitTransaction();
                            // realm.createOrUpdateAllFromJson(EventSponsor.class, response);
                            RealmResults eventSponsors = query.findAll();
                            callBack.fetchDidSucceed(eventSponsors);
                        } catch (Exception e) {
                            Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
                            callBack.fetchDidFail(e);
                            realm.commitTransaction();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(MMAConstants.TAG, "OnErrorRun()");
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        MMAApplication.getInstance().addToRequestQueue(req);
    }

    public static void makeAllObsolete(Realm realm) {
        RealmResults allItems = realm.where(EventSponsor.class).findAll();
        for (int i = 0; i < allItems.size(); i++) {
            EventSponsor item = (EventSponsor) allItems.get(i);
            item.setObsolete(true);
        }
    }

    public static void deleteAllObsolete(Realm realm) {
        RealmResults obsoleteItems = realm.where(EventSponsor.class).equalTo("isObsolete", true).findAll();
        for (int i = 0; i < obsoleteItems.size(); i++) {
            EventSponsor item = (EventSponsor) obsoleteItems.get(i);
            item.deleteFromRealm();
        }
    }
}

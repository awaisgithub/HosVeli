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
import io.realm.RealmCollection;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MuhammadMahmoor on 10/27/16.
 */

public class EventSpeaker extends RealmObject {
    @PrimaryKey
    String id;

    String dateModified;
    String speakerOrganization;
    String speaker;
    String eventTitle;
    String speakername;
    String remarks;
    String datecreated;
    String speakerorganization;
    String speakerJobTitle;
    String sortingSeq;
    String event;
    String dateCreated;
    String datemodified;
    String eventtitle;
    String speakerName;
    String speakerjobtitle;
    String sortingseq;

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getSpeakerOrganization() {
        return speakerOrganization;
    }

    public void setSpeakerOrganization(String speakerOrganization) {
        this.speakerOrganization = speakerOrganization;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getSpeakername() {
        return speakername;
    }

    public void setSpeakername(String speakername) {
        this.speakername = speakername;
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

    public String getSpeakerorganization() {
        return speakerorganization;
    }

    public void setSpeakerorganization(String speakerorganization) {
        this.speakerorganization = speakerorganization;
    }

    public String getSpeakerJobTitle() {
        return speakerJobTitle;
    }

    public void setSpeakerJobTitle(String speakerJobTitle) {
        this.speakerJobTitle = speakerJobTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortingSeq() {
        return sortingSeq;
    }

    public void setSortingSeq(String sortingSeq) {
        this.sortingSeq = sortingSeq;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDatemodified() {
        return datemodified;
    }

    public void setDatemodified(String datemodified) {
        this.datemodified = datemodified;
    }

    public String getEventtitle() {
        return eventtitle;
    }

    public void setEventtitle(String eventtitle) {
        this.eventtitle = eventtitle;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getSpeakerjobtitle() {
        return speakerjobtitle;
    }

    public void setSpeakerjobtitle(String speakerjobtitle) {
        this.speakerjobtitle = speakerjobtitle;
    }

    public String getSortingseq() {
        return sortingseq;
    }

    public void setSortingseq(String sortingseq) {
        this.sortingseq = sortingseq;
    }


    public static RealmList<EventSpeaker> getEventSpeakerList(Realm realm, String eventId) {
        RealmList<EventSpeaker> speakers = new RealmList<>();

         RealmResults realmResults = realm.where(EventSpeaker.class).equalTo("event", eventId).findAll();
        for(int i=0; i<realmResults.size(); i++) {
            speakers.add((EventSpeaker) realmResults.get(i));
        }
        return speakers;
    }

    public static RealmResults<EventSpeaker> getEventSpeakerResultSet(Realm realm, String eventId) {
        return realm.where(EventSpeaker.class).equalTo("event", eventId).findAll().sort("sortingseq", Sort.ASCENDING);
    }

    public static void fetchEventSpeakers(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(HRDFConstants.TAG, "fetchEventSpeakers ="+url);
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
                                    realm.createOrUpdateAllFromJson(EventSpeaker.class, response);
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

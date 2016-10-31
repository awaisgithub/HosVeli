package com.od.hrdf.BOs;

/**
 * Created by Awais on 10/21/2016.
 */

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
import io.realm.annotations.PrimaryKey;

public class SpeakerDocument extends RealmObject {
    @PrimaryKey
    private String id;

    private String event;
    private String speaker;
    private String dateModified;
    private String dateCreated;
    private RealmList<Document> documents = new RealmList<>();

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getEvent() { return event; }

    public void setEvent(String event) { this.event = event; }

    public String getSpeaker() { return speaker; }

    public void setSpeaker(String speaker) { this.speaker = speaker; }

    public RealmList<Document> getDocuments() { return documents; }

    public void setDocuments(RealmList<Document> documents) { this.documents = documents; }

    public RealmResults getSpeakerDocuments() {
        return documents.where().findAll();
    }
    public static SpeakerDocument getSpeakerDocument(Realm realm, String speakerId) {
        return realm.where(SpeakerDocument.class).equalTo("speaker", speakerId).findFirst();
    }

    public static RealmResults getSpeakerAllDocument(Realm realm, String speakerId) {
        return realm.where(SpeakerDocument.class).equalTo("speaker", speakerId).findAll();
    }

    public static void fetchSpeakerDocuments(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(HRDFConstants.TAG, "fetchSpeakerDocuments = "+url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    realm.beginTransaction();
                                    realm.createOrUpdateAllFromJson(SpeakerDocument.class, response);
                                    RealmResults eventDocumentId = query.findAll();
                                    callBack.fetchDidSucceed(eventDocumentId);
                                } catch (Exception e) {
                                    Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
                                    callBack.fetchDidFail(e);
                                }
                                realm.commitTransaction();
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
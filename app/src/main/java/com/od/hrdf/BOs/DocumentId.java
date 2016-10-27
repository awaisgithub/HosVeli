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

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class DocumentId extends RealmObject {
    @PrimaryKey
    private String id;
    private String event;
    private String speaker;
    private String dateModified;
    private String dateCreated;
    private RealmList<Document> documents;

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

    public static void fetchEventDocumentId(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        //      Log.i("AWAIS1", response.toString());
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    realm.beginTransaction();
                                    realm.createOrUpdateAllFromJson(DocumentId.class, response);
                                    RealmResults eventDocumentId = query.findAll();
                                    realm.commitTransaction();
                                    callBack.fetchDidSucceed(eventDocumentId);
                                } catch (Exception e) {
                                    Log.i("AWAIS1", "Exception Error - " + e.getMessage());
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
                        Log.i("AWAIS1", "OnErrorRun()");
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

}
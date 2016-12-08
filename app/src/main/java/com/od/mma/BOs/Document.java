package com.od.mma.BOs;

/**
 * Created by Awais on 10/21/2016.
 */

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

public class Document extends RealmObject {
    @PrimaryKey
    private String id;

    private String fileType;
    private String desc;
    private String file;
    private String documentId;
    private String dateModified;
    private String dateCreated;
    private String speaker;
    private String event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFiletype() {
        return fileType;
    }

    public void setFiletype(String fileType) {
        this.fileType = fileType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDocumentid() {
        return documentId;
    }

    public void setDocumentid(String documentId) {
        this.documentId = documentId;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

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

    public static RealmResults getAllDocumentForId(Realm realm, String docId) {
        return realm.where(Document.class).equalTo("documentId", docId).findAll();
    }

    public static void fetchDocument(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchDocument = "+url);
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
                                    realm.createOrUpdateAllFromJson(Document.class, response);
                                    RealmResults document = query.findAll();
                                    realm.commitTransaction();
                                    callBack.fetchDidSucceed(document);
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
        MMAApplication.getInstance().addToRequestQueue(req);
    }
}

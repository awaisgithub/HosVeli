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

public class SpeakerTopic extends RealmObject {
    @PrimaryKey
    private String id;

    private String title;
    private String desc;
    private String event;
    private String speaker;
    private String eventSpeakerId;
    private String dateModified;
    private String dateCreated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getEventSpeakerId() {
        return eventSpeakerId;
    }

    public void setEventSpeakerId(String eventSpeakerId) {
        this.eventSpeakerId = eventSpeakerId;
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

    public static RealmResults getSpeakerTopicForEvent(Realm realm, String speaker, String event) {
        return realm.where(SpeakerTopic.class).equalTo("speaker", speaker).equalTo("event", event).findAll();
    }

    public static void fetchEventTopics(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
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
                                    realm.createOrUpdateAllFromJson(SpeakerTopic.class, response);
                                    RealmResults speakerTopics = query.findAll();
                                    realm.commitTransaction();
                                    callBack.fetchDidSucceed(speakerTopics);
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

    public static void fetchEventSpeakerTopic(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchEventSpeakerTopic =" + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {

                        try {
                            realm.beginTransaction();
                            realm.createOrUpdateAllFromJson(SpeakerTopic.class, response);
                            RealmResults eventSpeakerTopic = query.findAll();
                            realm.commitTransaction();
                            callBack.fetchDidSucceed(eventSpeakerTopic);
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
                        Log.i(MMAConstants.TAG, "OnErrorRun()");
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        MMAApplication.getInstance().addToRequestQueue(req);
    }

}

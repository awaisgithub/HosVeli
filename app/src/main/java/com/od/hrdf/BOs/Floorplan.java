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
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Awais on 10/18/2016.
 */

public class Floorplan extends RealmObject {
    @PrimaryKey
    private String id;

    private String desc;
    private String event;
    private String floorPlan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
    }

    public static RealmResults getFloorPlanForEvent(Realm realm, String eventId) {
        return realm.where(Floorplan.class).equalTo("event", eventId).findAll();
    }

    public static void fetchEventFloorPlan(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(HRDFConstants.TAG, response.toString());

                        try {
                            realm.beginTransaction();
                            realm.createOrUpdateAllFromJson(Floorplan.class, response);
                            RealmResults floorPlan = query.findAll();
                            realm.commitTransaction();
                            callBack.fetchDidSucceed(floorPlan);
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

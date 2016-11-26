package com.od.hrdf.BOs;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.Utils.HRDFConstants;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
    private boolean isObsolete;

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

    public boolean isObsolete() {
        return isObsolete;
    }

    public void setObsolete(boolean obsolete) {
        isObsolete = obsolete;
    }

    public static Floorplan getFloorPlan(Realm realm, String id) {
        return realm.where(Floorplan.class).equalTo("id", id).findFirst();
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
                            Gson gson = new GsonBuilder().create();
                            Type collectionType = new TypeToken<ArrayList<Floorplan>>() {
                            }.getType();
                            ArrayList<Floorplan> floorPlans = gson.fromJson(response.toString(), collectionType);
                            if (floorPlans.size() > 0)
                                makeAllObsolete(realm);

                            for (Floorplan floorplan : floorPlans) {
                                Floorplan localSponsor = Floorplan.getFloorPlan(realm, floorplan.getId());
                                if (localSponsor != null) {
                                    floorplan.setObsolete(false);
                                } else {
                                }
                                realm.copyToRealmOrUpdate(floorplan);
                            }
                            deleteAllObsolete(realm);
                            //realm.createOrUpdateAllFromJson(Floorplan.class, response);
                            RealmResults floorPlan = query.findAll();
                            realm.commitTransaction();
                            callBack.fetchDidSucceed(floorPlan);
                        } catch (Exception e) {
                            Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
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
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

    public static void makeAllObsolete(Realm realm) {
        RealmResults allItems = realm.where(Floorplan.class).findAll();
        for (int i = 0; i < allItems.size(); i++) {
            Floorplan item = (Floorplan) allItems.get(i);
            item.setObsolete(true);
        }
    }

    public static void deleteAllObsolete(Realm realm) {
        RealmResults obsoleteItems = realm.where(Floorplan.class).equalTo("isObsolete", true).findAll();
        for (int i = 0; i < obsoleteItems.size(); i++) {
            Floorplan item = (Floorplan) obsoleteItems.get(i);
            item.deleteFromRealm();
        }
    }
}

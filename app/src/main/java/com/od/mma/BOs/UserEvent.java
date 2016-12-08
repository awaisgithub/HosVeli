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
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MuhammadMahmoor on 10/29/16.
 */

public class UserEvent extends RealmObject {

    @PrimaryKey
    private String event;

    private String contactNumber;
    private String eventTitle;
    private String designation;
    private String datecreated;
    private String type;
    private String isScan;
    private String id;
    private String contactnumber;
    private String isscan;
    private String company;
    private String name;
    private String rsvp;
    private String dateCreated;
    private String eventtitle;
    private String user;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsScan() {
        return isScan;
    }

    public void setIsScan(String isScan) {
        this.isScan = isScan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getIsscan() {
        return isscan;
    }

    public void setIsscan(String isscan) {
        this.isscan = isscan;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRsvp() {
        return rsvp;
    }

    public void setRsvp(String rsvp) {
        this.rsvp = rsvp;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEventtitle() {
        return eventtitle;
    }

    public void setEventtitle(String eventtitle) {
        this.eventtitle = eventtitle;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static UserEvent getUserEvent(Realm realm, String event) {
        return realm.where(UserEvent.class).equalTo("event", event).findFirst();
    }

    public static UserEvent getUserEvent(Realm realm, String event, String userId) {
        return realm.where(UserEvent.class).equalTo("event", event).equalTo("user", userId).findFirst();
    }

    public static RealmResults getAllUserEvents(Realm realm, String userId) {
        return realm.where(UserEvent.class).equalTo("user", userId).findAll().sort("datecreated", Sort.DESCENDING);
    }

    public static void fetchUserEvents(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchUserEvents URL =" + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {

                        realm.beginTransaction();
                        try {
                            Gson gson = new GsonBuilder().create();
                            Type collectionType = new TypeToken<ArrayList<UserEvent>>() {
                            }.getType();

                            ArrayList<UserEvent> userEventList = gson.fromJson(response.toString(), collectionType);

                            for (int i = 0; i < userEventList.size(); i++) {
                                Event event = Event.getUpcomingEventById(userEventList.get(i).getEvent(), realm);
                                if (event != null) {
                                    realm.copyToRealmOrUpdate(userEventList.get(i));
                                }
                            }

                            RealmResults users = query.findAll();

                            callBack.fetchDidSucceed(users);
                        } catch (Exception e) {
                            Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
                            callBack.fetchDidFail(e);
                        }
                        realm.commitTransaction();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                callBack.fetchDidFail(error);
            }
        });

        MMAApplication.getInstance().addToRequestQueue(req);
    }

}

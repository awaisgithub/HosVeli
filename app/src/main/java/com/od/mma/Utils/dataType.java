package com.od.mma.Utils;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.od.mma.API.Api;
import com.od.mma.BOs.Event;
import com.od.mma.MMAApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Awais on 10/17/2016.
 */

public class dataType extends RealmObject {
    private Date datecreated;

    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public static void checkTest(final Activity context, final Realm realm) {
        final Gson gson = new Gson();
        final JSONObject jo = new JSONObject();
        JsonArrayRequest req = new JsonArrayRequest(Api.urlUserEventList("andyoi90@gmail.com"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        //      Log.i(MMAConstants.TAG, response.toString());
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    realm.beginTransaction();


                                    //          realm.createOrUpdateAllFromJson(Event.class, response);
                                    RealmResults users =realm.where(Event.class).equalTo("user.email", "andyoi90@gmail.com").findAll();
//ERROR ERROR IllegalStateException
                                    Type collectionType = new TypeToken<ArrayList<Event>>() {
                                    }.getType();
                                    ArrayList<Event> events = gson.fromJson(response.toString(), collectionType);
                                    //       Log.i(MMAConstants.TAG, "AWAIS= "+events.size());



                                    realm.commitTransaction();
                                } catch (Exception e) {
                                    Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
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
                    }
                });
            }
        });

        MMAApplication.getInstance().addToRequestQueue(req);
    }

    public static Date toDate(String dateString) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            date = formatter.parse(dateString);
            Log.e("Print result: ", String.valueOf(date));

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }
}

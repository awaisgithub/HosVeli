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
import io.realm.Sort;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Awais on 10/18/2016.
 */

public class Agenda extends RealmObject {
    @PrimaryKey
    private String id;

    private String title;
    private String agendaDate;
    private String agendaStartTime;
    private String agendaEndTime;
    private String agendaType;
    private String session;
    private String event;
    private RealmList<SessionItem> sessionItems;
    // for GSON only
    @Ignore
    private String sessionItem;

    public RealmList<SessionItem> getSessionItems() {
        return sessionItems;
    }

    public void setSessionItems(RealmList<SessionItem> sessionItems) {
        this.sessionItems = sessionItems;
    }

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

    public String getAgendaDate() {
        return agendaDate;
    }

    public void setAgendaDate(String agendaDate) {
        this.agendaDate = agendaDate;
    }

    public String getAgendaStartTime() {
        return agendaStartTime;
    }

    public void setAgendaStartTime(String agendaStartTime) {
        this.agendaStartTime = agendaStartTime;
    }

    public String getAgendaEndTime() {
        return agendaEndTime;
    }

    public void setAgendaEndTime(String agendaEndTime) {
        this.agendaEndTime = agendaEndTime;
    }

    public String getAgendaType() {
        return agendaType;
    }

    public void setAgendaType(String agendaType) {
        this.agendaType = agendaType;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public RealmResults getSessionsForAgenda(Realm realm) {
//        return realm.where(SessionItem.class)..findAll();
        return sessionItems.where().findAll();
    }

    public static RealmResults getAgendaForDate(String event, String date, Realm realm) {
        return realm.where(Agenda.class).equalTo("agendaDate", date).equalTo("event", event).findAll().sort("agendaStartTime", Sort.ASCENDING);
    }

    public static void fetchEventAgenda(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchEventAgenda ="+url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    realm.beginTransaction();
                                    Gson gson = new GsonBuilder().create();
                                    Type collectionType = new TypeToken<ArrayList<Agenda>>() {
                                    }.getType();

                                    ArrayList<Agenda> agendaList = gson.fromJson(response.toString(), collectionType);
                                    for(Agenda agenda : agendaList) {
                                        String sessionsJSON = agenda.sessionItem.replace("'\'", " ");
                                        collectionType = new TypeToken<RealmList<SessionItem>>() {
                                        }.getType();

                                        RealmList<SessionItem> agendaSessionList = gson.fromJson(sessionsJSON, collectionType);
                                        agenda.setSessionItems(agendaSessionList);
                                        realm.copyToRealmOrUpdate(agenda);
                                    }
//                                    realm.createOrUpdateAllFromJson(Agenda.class, response);
                                    RealmResults agenda = query.findAll();
                                    callBack.fetchDidSucceed(agenda);
                                } catch (Exception e) {
                                    Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
                                    callBack.fetchDidFail(e);
                                } finally {
                                    realm.commitTransaction();
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
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        MMAApplication.getInstance().addToRequestQueue(req);
    }
}

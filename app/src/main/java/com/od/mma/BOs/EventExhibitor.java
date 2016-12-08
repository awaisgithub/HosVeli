package com.od.mma.BOs;

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
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MuhammadMahmoor on 10/28/16.
 */

public class EventExhibitor extends RealmObject {

    @PrimaryKey
    private String id;

    private String dateModified;
    private String exhibitorCategorySeq;
    private String exhibitorWebsite;
    private String eventTitle;
    private String remarks;
    private String datecreated;
    private String boothNo;
    private String exhibitorCategoryName;
    @Index
    private String exhibitorcategoryseq;

    private String boothno;
    private String exhibitorName;
    private String event;
    private String dateCreated;
    private String exhibitorcategoryname;
    private String exhibitorname;
    private String datemodified;
    private String exhibitorCategory;
    private String exhibitorcategory;
    private String eventtitle;
    private String exhibitorwebsite;
    private String exhibitor;
    private String description;
    boolean isObsolete;

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getExhibitorCategorySeq() {
        return exhibitorCategorySeq;
    }

    public void setExhibitorCategorySeq(String exhibitorCategorySeq) {
        this.exhibitorCategorySeq = exhibitorCategorySeq;
    }

    public String getExhibitorWebsite() {
        return exhibitorWebsite;
    }

    public void setExhibitorWebsite(String exhibitorWebsite) {
        this.exhibitorWebsite = exhibitorWebsite;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
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

    public String getBoothNo() {
        return boothNo;
    }

    public void setBoothNo(String boothNo) {
        this.boothNo = boothNo;
    }

    public String getExhibitorCategoryName() {
        return exhibitorCategoryName;
    }

    public void setExhibitorCategoryName(String exhibitorCategoryName) {
        this.exhibitorCategoryName = exhibitorCategoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExhibitorcategoryseq() {
        return exhibitorcategoryseq;
    }

    public void setExhibitorcategoryseq(String exhibitorcategoryseq) {
        this.exhibitorcategoryseq = exhibitorcategoryseq;
    }

    public String getBoothno() {
        return boothno;
    }

    public void setBoothno(String boothno) {
        this.boothno = boothno;
    }

    public String getExhibitorName() {
        return exhibitorName;
    }

    public void setExhibitorName(String exhibitorName) {
        this.exhibitorName = exhibitorName;
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

    public String getExhibitorcategoryname() {
        return exhibitorcategoryname;
    }

    public void setExhibitorcategoryname(String exhibitorcategoryname) {
        this.exhibitorcategoryname = exhibitorcategoryname;
    }

    public String getExhibitorname() {
        return exhibitorname;
    }

    public void setExhibitorname(String exhibitorname) {
        this.exhibitorname = exhibitorname;
    }

    public String getDatemodified() {
        return datemodified;
    }

    public void setDatemodified(String datemodified) {
        this.datemodified = datemodified;
    }

    public String getExhibitorCategory() {
        return exhibitorCategory;
    }

    public void setExhibitorCategory(String exhibitorCategory) {
        this.exhibitorCategory = exhibitorCategory;
    }

    public String getExhibitorcategory() {
        return exhibitorcategory;
    }

    public void setExhibitorcategory(String exhibitorcategory) {
        this.exhibitorcategory = exhibitorcategory;
    }

    public String getEventtitle() {
        return eventtitle;
    }

    public void setEventtitle(String eventtitle) {
        this.eventtitle = eventtitle;
    }

    public String getExhibitorwebsite() {
        return exhibitorwebsite;
    }

    public void setExhibitorwebsite(String exhibitorwebsite) {
        this.exhibitorwebsite = exhibitorwebsite;
    }

    public String getExhibitor() {
        return exhibitor;
    }

    public void setExhibitor(String exhibitor) {
        this.exhibitor = exhibitor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isObsolete() {
        return isObsolete;
    }

    public void setObsolete(boolean obsolete) {
        isObsolete = obsolete;
    }

    public static RealmList<EventExhibitor> getEventExhibitorList(Realm realm, String eventId) {
        RealmList<EventExhibitor> eventExhibitors = new RealmList<>();

        RealmResults realmResults = realm.where(EventExhibitor.class).equalTo("event", eventId).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            eventExhibitors.add((EventExhibitor) realmResults.get(i));
        }
        return eventExhibitors;
    }


    public static EventExhibitor getExhibitorForId(Realm realm, String id) {
        return realm.where(EventExhibitor.class).equalTo("id", id).findFirst();
    }

    public static RealmResults getExhibitorForCategory(Realm realm, String category, String eventId) {
        return realm.where(EventExhibitor.class).equalTo("exhibitorcategoryname", category).equalTo("isObsolete", false).equalTo("event", eventId).findAll().sort("exhibitorName", Sort.ASCENDING);
    }

    public static RealmResults getEventExhibitor(Realm realm, String eventId) {
        return realm.where(EventExhibitor.class).equalTo("event", eventId).equalTo("isObsolete", false).findAll().distinct("exhibitorcategoryseq").sort("exhibitorcategoryseq", Sort.ASCENDING);
    }

    public static void fetchEventExhibitors(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchEventExhibitors = " + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(MMAConstants.TAG, response.toString());
                        try {
                            makeAllOrdersObsolete(realm);
                            for (int i = 0; i < response.length(); i++) {
                                String id = response.getJSONObject(i).optString("id");
                                EventExhibitor exhibitor = getExhibitorForId(realm, id);
                                if (exhibitor != null) {
                                    realm.beginTransaction();
                                    exhibitor.setObsolete(false);
                                    realm.createOrUpdateObjectFromJson(EventExhibitor.class, response.getJSONObject(i));
                                    realm.commitTransaction();
                                } else {
                                    realm.beginTransaction();
                                    exhibitor = realm.createObjectFromJson(EventExhibitor.class, response.getJSONObject(i));
                                    realm.commitTransaction();
                                }
                            }
                            deleteAllObsoleteExhibitors(realm);
                            RealmResults eventExhibitor = query.findAll();
                            callBack.fetchDidSucceed(eventExhibitor);
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

    public static void makeAllOrdersObsolete(Realm realm) {
        RealmResults allExhibitor = realm.where(EventExhibitor.class).findAll();
        realm.beginTransaction();
        for (int i = 0; i < allExhibitor.size(); i++) {
            EventExhibitor order = (EventExhibitor) allExhibitor.get(i);
            order.setObsolete(true);
        }
        realm.commitTransaction();
    }

    public static void deleteAllObsoleteExhibitors(Realm realm) {
        RealmResults obsoleteOrders = realm.where(EventExhibitor.class).equalTo("isObsolete", true).findAll();
        realm.beginTransaction();
        for (int i = 0; i < obsoleteOrders.size(); i++) {
            EventExhibitor order = (EventExhibitor) obsoleteOrders.get(i);
            order.deleteFromRealm();
        }
        realm.commitTransaction();
    }
}

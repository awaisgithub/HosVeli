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
 * Created by Awais on 10/10/2016.
 */

public class Branches extends RealmObject {
    @PrimaryKey
    private String id;

    private String phone;
    private String dateModified;
    private String mainOffice;
    private String fax;
    private String address;
    private String branchManager;
    private String name;
    private String dateCreated;
    private String branchManagerEmail;
    boolean isObsolete;

    public Branches() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getMainOffice() {
        return mainOffice;
    }

    public void setMainOffice(String mainOffice) {
        this.mainOffice = mainOffice;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBranchManager() {
        return branchManager;
    }

    public void setBranchManager(String branchManager) {
        this.branchManager = branchManager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getBranchManagerEmail() {
        return branchManagerEmail;
    }

    public void setBranchManagerEmail(String branchManagerEmail) {
        this.branchManagerEmail = branchManagerEmail;
    }

    public boolean isObsolete() {
        return isObsolete;
    }

    public void setObsolete(boolean obsolete) {
        isObsolete = obsolete;
    }

    //METHODS

    public static Branches getBranch(Realm realm, String id) {
        return realm.where(Branches.class).equalTo("id", id).findFirst();
    }

    public static RealmResults<Branches> getBranches(Realm realm) {
        return realm.where(Branches.class).findAll().sort("name", Sort.ASCENDING);
    }

    public static void fetchBranches(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchArticles = " + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {

                        realm.beginTransaction();
                        try {
                            Gson gson = new GsonBuilder().create();
                            Type collectionType = new TypeToken<ArrayList<Branches>>() {
                            }.getType();
                            ArrayList<Branches> branches = gson.fromJson(response.toString(), collectionType);
                            if (branches.size() > 0)
                                makeAllItemsObsolete(realm);

                            for (Branches branch: branches) {
                                Branches localBranches = Branches.getBranch(realm, branch.getId());
                                if (localBranches != null) {
                                    branch.setObsolete(false);
                                } else {
                                }
                                realm.copyToRealmOrUpdate(branch);
                            }
                            deleteAllObsoleteItems(realm);
//                                    realm.copyToRealmOrUpdate(articles);//Article.class, response);
                            RealmResults articlesResultSet = query.findAll();
                            callBack.fetchDidSucceed(articlesResultSet);
                        } catch (Exception e) {
                            Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
                            callBack.fetchDidFail(e);
                        }
                        realm.commitTransaction();
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
        // Adding request to request queue
        MMAApplication.getInstance().addToRequestQueue(req);
    }

    public static void makeAllItemsObsolete(Realm realm) {
        RealmResults allItems = realm.where(Branches.class).findAll();
        for (int i = 0; i < allItems.size(); i++) {
            Branches article = (Branches) allItems.get(i);
            article.setObsolete(true);
        }
    }

    public static void deleteAllObsoleteItems(Realm realm) {
        RealmResults obsoleteItems = realm.where(Branches.class).equalTo("isObsolete", true).findAll();
        for (int i = 0; i < obsoleteItems.size(); i++) {
            Branches article = (Branches) obsoleteItems.get(i);
            article.deleteFromRealm();
        }
    }

}

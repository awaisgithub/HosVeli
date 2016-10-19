
package com.od.hrdf.BOs;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.od.hrdf.CallBack.CheckCallBack;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.Utils.HRDFConstants;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

import static android.R.attr.data;

/**
 * Created by Awais on 10/10/2016.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String email;

    private int age;
    private boolean is_hrdf_staff;
    private int postcode;
    private String address11;
    private String address12;
    private String city;
    private String company;
    private String contactNumber;
    private String designation;
    private String employmentstatus;
    private String gender;
    private String industry;
    private String mycoid;
    private String name;
    private String nationality;
    private String passport;
    private String password;
    private String race;
    private String salutation;
    private String sector;
    private String state;
    private String status;
    private boolean isSynced = false;
    private boolean isTemp = true;
    private RealmList<Event> events;

    public User() {
    }

    public String getAddress11() {
        return address11;
    }

    public void setAddress11(String address11) {
        this.address11 = address11;
    }

    public String getAddress12() {
        return address12;
    }

    public void setAddress12(String address12) {
        this.address12 = address12;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmploymentstatus() {
        return employmentstatus;
    }

    public void setEmploymentstatus(String employmentstatus) {
        this.employmentstatus = employmentstatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean temp) {
        isTemp = temp;
    }

    public boolean is_hrdf_staff() {
        return is_hrdf_staff;
    }

    public void setIs_hrdf_staff(boolean is_hrdf_staff) {
        this.is_hrdf_staff = is_hrdf_staff;
    }

    public String getMycoid() {
        return mycoid;
    }

    public void setMycoid(String mycoid) {
        this.mycoid = mycoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //METHODS

    private static User getTempUser(Realm realm) {
        return realm.where(User.class).equalTo("isTemp", true).equalTo("isSynced", false)
                .findFirst();
    }

    public static User getTempOrNewUser(Realm realm) {
        User tempUser = getTempUser(realm);
        if (tempUser == null) {
            tempUser = realm.createObject(User.class);
        }
        return tempUser;
    }

    public void update(Realm realm) {
        try {
            realm.createObject(User.class);
        } catch (Exception NSError) {
            System.out.println("Failed to save user");
        }
    }

    public static User getUser(String id, Realm realm) {
        return realm.where(User.class).equalTo("email", id)
                .findFirst();
    }

    public static User getCurrentUser(Realm realm) {
        return realm.where(User.class).equalTo("isTemp", false).equalTo("isSynced", true)
                .findFirst();
    }

    public Pair<Boolean, String> validate() {
        if (name == null || name.isEmpty())
            return new Pair<Boolean, String>(false, "Please provide your full name.");

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return new Pair<Boolean, String>(false, "Please provide a valid email.");

        if (!Patterns.PHONE.matcher(contactNumber).matches())
            return new Pair<Boolean, String>(false, "Please provide a valid contact number.");

        if (nationality.isEmpty() || nationality == null)
            return new Pair<Boolean, String>(false, "Please provide your nationality.");

        if (password.isEmpty() || password == null)
            return new Pair<Boolean, String>(false, "You must provide password to sign up.");

        return new Pair<Boolean, String>(true, "");
    }

    public JSONObject getUserData() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", email);
        jsonObject.put("name", name);
        jsonObject.put("contactNumber", contactNumber);
        jsonObject.put("nationality", nationality);
        jsonObject.put("password", password);
        jsonObject.put("type", "User");
        return jsonObject;
    }

    public static RealmResults<Event> getUserEvents(RealmResults delegate, String userId, Realm realm) {
        return realm.where(Event.class).equalTo("email", userId)
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static void fetchUser(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(HRDFConstants.TAG, response.toString());

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response != null && response.length() > 0) {
                                    try {
                                        realm.beginTransaction();
                                        realm.createOrUpdateAllFromJson(User.class, response);
                                        RealmResults user = query.findAll();
                                        realm.commitTransaction();
                                        callBack.fetchDidSucceed(user);
                                    } catch (Exception e) {
                                        Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
                                        callBack.fetchDidFail(e);
                                    }
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
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

    public static void checkDuplicate(final Activity context, final Realm realm, String url, final RealmQuery query, final CheckCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(HRDFConstants.TAG, response.toString());

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response != null && response.length() > 0) {
                                    try {
                                        boolean duplicate = false;
                                        RealmResults user = query.findAll();
                                        if (user.size() > 0)
                                            duplicate = true;
                                        callBack.checkDuplicateUser(duplicate);
                                    } catch (Exception e) {
                                        Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
                                        callBack.checkFail(e);
                                    }
                                } else {
                                    callBack.checkDuplicateUser(false);
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
                        callBack.checkFail(error);
                    }
                });
            }
        });
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

    public static void performUserRegistration(Activity context, String url, FetchCallBack callBack) {
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest userRegistration;
        try {
            jsonObject.accumulate("sector", "A");
            jsonObject.accumulate("passwd", "OpenDynamics");
            jsonObject.accumulate("dateModified", "2016-10-14 15:54:56.0");
            jsonObject.accumulate("contactNumber", "010");
            jsonObject.accumulate("isSynced", "");
            jsonObject.accumulate("state", "Selangor");
            jsonObject.accumulate("designation", "CEO");
            jsonObject.accumulate("address1", "Malaysia kelana");
            jsonObject.accumulate("mycoid", "");
            jsonObject.accumulate("address2", "");
            jsonObject.accumulate("type", "User");
            jsonObject.accumulate("is_hrdf_staff", "");
            jsonObject.accumulate("race", "Black");
            jsonObject.accumulate("city", "kuala lumpur");
            jsonObject.accumulate("id", "name@person.com");
            jsonObject.accumulate("name", "sameer");
            jsonObject.accumulate("age", "2016-10-14 15:54:56.0");
            jsonObject.accumulate("gender", "A");
            jsonObject.accumulate("industry", "OpenDynamics");
            jsonObject.accumulate("status", "2016-10-14 15:54:56.0");
            jsonObject.accumulate("postcode", "A");
            jsonObject.accumulate("nationality", "OpenDynamics");
            jsonObject.accumulate("company", "2016-10-14 15:54:56.0");
            jsonObject.accumulate("dateCreated", "A");
            jsonObject.accumulate("salutation", "OpenDynamics");
            jsonObject.accumulate("passport", "2016-10-14 15:54:56.0");
            jsonObject.accumulate("employmentStatus", "2016-10-14 15:54:56.0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(HRDFConstants.TAG, "JsonObject= " + jsonObject.toString());
        userRegistration = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(HRDFConstants.TAG, " onResponse = " + response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(HRDFConstants.TAG, " ERRor  Errror ERROR = " + error.toString());

            }
        });
        HRDFApplication.getInstance().addToRequestQueue(userRegistration);
    }

    public static void forgotPassword(Activity context, String url, final String Email) {
        String jStr = "{\"email\":\"" + Email + "\"}";
        JSONObject object = null;
        try {
            object = new JSONObject(jStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(HRDFConstants.TAG, " onResponse = " + response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(HRDFConstants.TAG, " ERRor  Errror ERROR = " + error.toString());

            }
        });
        HRDFApplication.getInstance().addToRequestQueue(jr);
    }

}


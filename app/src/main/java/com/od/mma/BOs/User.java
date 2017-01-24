
package com.od.mma.BOs;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.od.mma.CallBack.CheckCallBack;
import com.od.mma.CallBack.LoginCallBack;
import com.od.mma.CallBack.ServerReadCallBack;
import com.od.mma.CallBack.StatusCallBack;
import com.od.mma.MMAApplication;
import com.od.mma.Membership.Membership;
import com.od.mma.Payload.JSONPayloadManager;
import com.od.mma.R;
import com.od.mma.Utils.MMAConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Awais on 10/10/2016.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String id;

    //MMA
    private String fname;
    private String lname;
    private String email;
    private String applicantPassword;
    private String confirmPassword;
    private String gcm_id = " ";
    private boolean isSyncedLocal = false;
    private boolean isTemp = true;
    //


    private String age;
    private String is_hrdf_staff;
    private String postcode;
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
    private String race;
    private String salutation;
    private String sector;
    private String state;
    private String status;
    private String photo;
    private RealmList<UserEvent> events = new RealmList<>();

    public User() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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

    public boolean isSyncedLocal() {
        return isSyncedLocal;
    }

    public void setSyncedLocal(boolean syncedLocal) {
        isSyncedLocal = syncedLocal;
    }

    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean temp) {
        isTemp = temp;
    }

    public String is_hrdf_staff() {
        return is_hrdf_staff;
    }

    public void setIs_hrdf_staff(String is_hrdf_staff) {
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

    public String getApplicantPassword() {
        return applicantPassword;
    }

    public void setApplicantPassword(String applicantPassword) {
        this.applicantPassword = applicantPassword;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_hrdf_staff() {
        return is_hrdf_staff;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public RealmList<UserEvent> getEvents() {
        return events;
    }

    public void setEvents(RealmList<UserEvent> events) {
        this.events = events;
    }

    //METHODS


    //MMA
    public Pair<Boolean, String> validate() {
        if (fname == null || fname.isEmpty())
            return new Pair<Boolean, String>(false, MMAApplication.context.getResources().getString(R.string.reg_error_fname));

        if (lname == null || lname.isEmpty())
            return new Pair<Boolean, String>(false, MMAApplication.context.getResources().getString(R.string.reg_error_lname));


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches())
            return new Pair<Boolean, String>(false, MMAApplication.context.getResources().getString(R.string.reg_error_email));

        if (applicantPassword.isEmpty() || applicantPassword == null)
            return new Pair<Boolean, String>(false, MMAApplication.context.getResources().getString(R.string.reg_error_password));

        if (!applicantPassword.equalsIgnoreCase(confirmPassword)) {
            return new Pair<Boolean, String>(false, MMAApplication.context.getResources().getString(R.string.reg_error_password_not_match));
        }

        return new Pair<Boolean, String>(true, "");
    }


    public static User getCurrentUser(Realm realm) {
        return realm.where(User.class).equalTo("isTemp", false).equalTo("isSyncedLocal", true)
                .findFirst();
    }

    public static void performUserRegistration(String url, final StatusCallBack callBack) {
        Log.i(MMAConstants.TAG_MMA, "performUserRegistration =" + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(MMAConstants.TAG_MMA, "Sign-up reposnse(success) = " + response.toString());
                callBack.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(MMAConstants.TAG_MMA, "Sign-up reposnse(error) = " + error.toString());
                callBack.failure(error.toString());
            }
        });
        MMAApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public static void performLogin(String url, final Activity context, final LoginCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchUser =" + url);

        JsonObjectRequest login_req = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(MMAConstants.TAG_MMA, "LOGIN response = " + response.toString());
                callBack.fetchDidSucceed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(MMAConstants.TAG_MMA, "LOGIN response = " + error.toString());
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        MMAApplication.getInstance().addToRequestQueue(login_req);
    }

    public static void forgotPassword(String url, final Activity context, final StatusCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchUser =" + url);

        JsonObjectRequest login_req = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(MMAConstants.TAG_MMA, "LOGIN response = " + response.toString());
                callBack.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(MMAConstants.TAG_MMA, "LOGIN response = " + error.toString());
                        callBack.failure(error.toString());
                    }
                });
            }
        });
        MMAApplication.getInstance().addToRequestQueue(login_req);
    }

    public static void performMembershipUpdation(User user, Membership membership, String url, final StatusCallBack callBack) throws JSONException {
        Log.i(MMAConstants.TAG, "performUserUpdation=" + url);
        JSONObject jsonObject = JSONPayloadManager.getInstance().getMembershipPayload(user, membership);
        JsonObjectRequest userUpdation = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.success(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.failure(error.toString());

            }
        });
        MMAApplication.getInstance().addToRequestQueue(userUpdation);

    }

    public static void getApplicantData(String url, final ServerReadCallBack callBack) {
        Log.i(MMAConstants.TAG, "getApplicantData = " + url);
        JsonArrayRequest applicantData = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                if (response.toString().contains("id")) {
                    Log.i(MMAConstants.TAG_MMA, "APPLICANT RESPONSE1 = " + response.toString());
                    callBack.success(response);
                } else if (response.toString().contains("[]")) {
                    Log.i(MMAConstants.TAG_MMA, "APPLICANT RESPONSE1 = No Entry exist for this username");
                    callBack.failure("No Entry exist for this username");
                } else if (response.toString().contains("error")) {
                    Log.i(MMAConstants.TAG_MMA, "APPLICANT RESPONSE = Table doesnot exit");
                    callBack.failure("Table doesnot exit");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(MMAConstants.TAG_MMA, "APPLICANT RESPONSE2 = " + error.toString());
                callBack.failure(error.toString());
            }
        });
        MMAApplication.getInstance().addToRequestQueue(applicantData);
    }

    public static void getSpinnerList(String url, final ServerReadCallBack callBack) {

        JsonArrayRequest titlesList = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.toString().length() > 2)
                    callBack.success(response);
                else
                    callBack.failure("empty");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.failure(error.toString());
            }
        });
        MMAApplication.getInstance().addToRequestQueue(titlesList);
    }

    //


    public RealmResults getUserEvents() {
        return events.where().findAll();
    }

    private static User getTempUser(Realm realm) {
        return realm.where(User.class).equalTo("isTemp", true).equalTo("isSyncedLocal", false)
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
        return realm.where(User.class).equalTo("id", id)
                .findFirst();
    }


    public JSONObject getUserData() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("contactNumber", contactNumber);
        jsonObject.put("nationality", nationality);
        jsonObject.put("applicantPassword", applicantPassword);
        jsonObject.put("type", "User");
        return jsonObject;
    }

    public static RealmResults<Event> getUserEvents(String userId, Realm realm) {
        return realm.where(Event.class).equalTo("id", userId)
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static void fetchUser(final Activity context, final Realm realm, String url, final RealmQuery query, final LoginCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchUser =" + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(MMAConstants.TAG, response.toString());
                        //    callBack.fetchDidSucceed(response.toString());
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

    public static void checkDuplicate(String url, final CheckCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(MMAConstants.TAG, response.toString());

                        if (response != null && response.length() > 0) {
                            callBack.checkDuplicateUser(true);
                        } else {
                            callBack.checkDuplicateUser(false);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                callBack.checkFail(error);
            }
        });
        MMAApplication.getInstance().addToRequestQueue(req);
    }

    public static void performUserRegistration(User user, String url, final StatusCallBack callBack) {
        Log.i(MMAConstants.TAG, "performUserRegistration =" + url);
        JSONObject jsonObject = JSONPayloadManager.getInstance().getRegReqPayload(user);
        JsonObjectRequest userRegistration = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.success(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.failure(error.toString());

            }
        });
        MMAApplication.getInstance().addToRequestQueue(userRegistration);
    }

    public static void performUserUpdation(User user, String url, final StatusCallBack callBack) {
        Log.i(MMAConstants.TAG, "performUserUpdation=" + url);
        JSONObject jsonObject = JSONPayloadManager.getInstance().getRegReqPayloadUpdation(user);
        JsonObjectRequest userUpdation = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.success(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.failure(error.toString());

            }
        });
        MMAApplication.getInstance().addToRequestQueue(userUpdation);
    }

    public static void addGCMId(String userId, String gcmId, String url, final StatusCallBack callBack) {
        JSONObject jsonObject = JSONPayloadManager.getInstance().getGCMUploadPayload(userId, gcmId);
        JsonObjectRequest updateGCMId = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.success(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.failure(error.toString());

            }
        });
        MMAApplication.getInstance().addToRequestQueue(updateGCMId);
    }

    public static void updateGCMId(String userId, String gcmId, String url, final StatusCallBack callBack) {
        JSONObject jsonObject = JSONPayloadManager.getInstance().getUpdateGCMIDPayload(userId, gcmId);
        JsonObjectRequest updateGCMId = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.success(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.failure(error.toString());

            }
        });
        MMAApplication.getInstance().addToRequestQueue(updateGCMId);
    }


}


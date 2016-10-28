package com.od.hrdf.BOs;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.od.hrdf.CallBack.LocationCallBack;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.CallBack.StatusCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.Payload.JSONPayloadManager;
import com.od.hrdf.Utils.HRDFConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import io.realm.Realm;

/**
 * Created by Awais on 10/10/2016.
 */

public class Event extends RealmObject {
    @PrimaryKey
    private String id;
    @Nullable
    private Long sequence;

    private String contactEmail;
    private Date endDate;
    private String endTime;
    private String facebookLink;
    private String feeEarlybirdGroup;
    private String feeEarlybirdIndividual;
    private String feeGovernment;
    private String feeNormalGroup;
    private String feeNormalIndividual;
    private String feeStudent;
    private String image;
    private String instagramLink;
    private String linkedInLink;
    private String location;
    private String objective;
    private String organizer;
    private String overview;
    private String privacyListing;
    private Date startDate;
    private String startTime;
    private String title;
    private String theme;
    private String twitterLink;
    private String weChatLink;
    private String feedBackId;
    private RealmList<EventSpeaker> speakers;
    private RealmList<EventExhibitor> exhibitors;
    private RealmList<EventSponsor> sponsors;
    private RealmList<Agenda> agenda;
    private RealmList<Floorplan> floorplans;
    private boolean isFavourite;
    private boolean isBookedLocal;
    private String userId;
    private User userObj;

    public Event() {
        userObj = new User();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getFeeEarlybirdGroup() {
        return feeEarlybirdGroup;
    }

    public void setFeeEarlybirdGroup(String feeEarlybirdGroup) {
        this.feeEarlybirdGroup = feeEarlybirdGroup;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeeEarlybirdIndividual() {
        return feeEarlybirdIndividual;
    }

    public void setFeeEarlybirdIndividual(String feeEarlybirdIndividual) {
        this.feeEarlybirdIndividual = feeEarlybirdIndividual;
    }

    public void setSequence(@Nullable Long sequence) {
        this.sequence = sequence;
    }

    public RealmList<Agenda> getAgenda() {
        return agenda;
    }

    public void setAgenda(RealmList<Agenda> agenda) {
        this.agenda = agenda;
    }

    public RealmList<Floorplan> getFloorplans() {
        return floorplans;
    }

    public void setFloorplans(RealmList<Floorplan> floorplans) {
        this.floorplans = floorplans;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public User getUserObj() {
        return userObj;
    }

    public void setUserObj(User userObj) {
        this.userObj = userObj;
    }

    public String getFeeGovernment() {
        return feeGovernment;
    }

    public void setFeeGovernment(String feeGovernment) {
        this.feeGovernment = feeGovernment;
    }

    public String getFeeNormalGroup() {
        return feeNormalGroup;
    }

    public void setFeeNormalGroup(String feeNormalGroup) {
        this.feeNormalGroup = feeNormalGroup;
    }

    public String getFeeNormalIndividual() {
        return feeNormalIndividual;
    }

    public void setFeeNormalIndividual(String feeNormalIndividual) {
        this.feeNormalIndividual = feeNormalIndividual;
    }

    public String getFeeStudent() {
        return feeStudent;
    }

    public void setFeeStudent(String feeStudent) {
        this.feeStudent = feeStudent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getLinkedInLink() {
        return linkedInLink;
    }

    public void setLinkedInLink(String linkedInLink) {
        this.linkedInLink = linkedInLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPrivacyListing() {
        return privacyListing;
    }

    public void setPrivacyListing(String privacyListing) {
        this.privacyListing = privacyListing;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getWeChatLink() {
        return weChatLink;
    }

    public void setWeChatLink(String weChatLink) {
        this.weChatLink = weChatLink;
    }

    public String getFeedBackId() {
        return feedBackId;
    }

    public void setFeedBackId(String feedBackId) {
        this.feedBackId = feedBackId;
    }

    @Nullable
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public RealmList<EventSpeaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(RealmList<EventSpeaker> speakers) {
        this.speakers = speakers;
    }

    public RealmList<EventExhibitor> getExhibitors() {
        return exhibitors;
    }

    public void setExhibitors(RealmList<EventExhibitor> exhibitors) {
        this.exhibitors = exhibitors;
    }

    public boolean getBooked() {
        return isBookedLocal;
    }

    public void setBooked(boolean booked) {
        isBookedLocal = booked;
    }

    public RealmList<EventSponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(RealmList<EventSponsor> sponsors) {
        this.sponsors = sponsors;
    }

    //METHODS
    public static Event getEvent(String id, Realm realm) {
        return realm.where(Event.class).equalTo("id", id).findFirst();
    }

    public static RealmResults<Event> getUpcomingEvents(Realm realm) {
        Date today = new Date();
        return realm.where(Event.class).greaterThanOrEqualTo("endDate", today)
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static RealmResults<Event> getPastEvents(Realm realm) {
        Date today = new Date();
        return realm.where(Event.class).lessThan("endDate", today)
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static RealmResults<Event> getSurveyEvents(RealmResults delegate, Realm realm) {
        Date today = new Date();
        return realm.where(Event.class).lessThan("endDate", today).isNotNull("feedBackId").isNotEmpty("feedBackId")
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static void fetchAllEvents(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(HRDFConstants.TAG, "fetchAllEvents URL =" + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(HRDFConstants.TAG, response.toString());
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
                                Type collectionType = new TypeToken<ArrayList<Event>>() {
                                }.getType();

                                ArrayList<Event> eventList = gson.fromJson(response.toString(), collectionType);
                                String jsonString = gson.toJson(eventList);
                                Log.i(HRDFConstants.TAG, "jsonString - " + jsonString);
                                realm.beginTransaction();
                                for (Event event : eventList) {
                                    Event localEvent = getEvent(event.getId(), realm);
                                    if(localEvent != null) {
                                        boolean isBooked = localEvent.getBooked();
                                        if(isBooked)
                                            event.setBooked(true);
                                    }
                                    realm.copyToRealmOrUpdate(event);
                                }
                                realm.commitTransaction();
                                RealmResults events = query.findAll();
                                try {
                                    callBack.fetchDidSucceed(events);
                                } catch (Exception e) {
                                    e.printStackTrace();
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

    public static void fetchUserEvents(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();

                                    RealmResults users = query.findAll();

                                    Type collectionType = new TypeToken<ArrayList<UserEventBO>>() {
                                    }.getType();

                                    ArrayList<UserEventBO> userEvents = gson.fromJson(response.toString(), collectionType);
                                    String jsonString = gson.toJson(userEvents);

                                    for (UserEventBO event : userEvents) {
                                        Log.i(HRDFConstants.TAG, "Date Created =     " + event.datecreated.toString());
                                    }

                                    callBack.fetchDidSucceed(users);
                                } catch (Exception e) {
                                    Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
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
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });

        HRDFApplication.getInstance().addToRequestQueue(req);
    }

    public static void fetchEventLocation(final Activity context, String url, final String address, final LocationCallBack callBack) {
        String cordinates = "";
        double latitude, longitude;
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0 && addresses != null) {
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
            cordinates = "Latitude = " + String.valueOf(latitude) + " , Longitude = " + String.valueOf(longitude);
            callBack.locationCordinates(cordinates);
        }
    }

    public static void bookEvent(String userId, String eventId, String url, final StatusCallBack callBack) {
        Log.i(HRDFConstants.TAG, "performUserRegistration =" + url);
        JSONObject jsonObject = JSONPayloadManager.getInstance().getRSVPReqPayload(eventId, userId);
        JsonObjectRequest rsvpRegistration = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
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
        HRDFApplication.getInstance().addToRequestQueue(rsvpRegistration);
    }
}

package com.od.mma.BOs;

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
import com.od.mma.CallBack.LocationCallBack;
import com.od.mma.CallBack.FetchCallBack;
import com.od.mma.CallBack.StatusCallBack;
import com.od.mma.MMAApplication;
import com.od.mma.Payload.JSONPayloadManager;
import com.od.mma.Utils.MMAConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Date endDateTime;
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
    private String surveyId;
    private String locationLongitude = "";
    private String locationLatitude = "";
    private RealmList<EventSpeaker> speakers;
    private RealmList<EventExhibitor> exhibitors;
    private RealmList<EventSponsor> sponsors;
    private RealmList<Agenda> agenda;
    private RealmList<Floorplan> floorplans;
    private boolean isFavourite;
    private boolean isBookedLocal;
    private String userId;
    private User userObj;
    private boolean isObsolete;


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

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
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

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public RealmList<EventSponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(RealmList<EventSponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public boolean isObsolete() {
        return isObsolete;
    }

    public void setObsolete(boolean obsolete) {
        isObsolete = obsolete;
    }

    //METHODS
    public static String getEventName(String id, Realm realm) {
        return realm.where(Event.class).equalTo("id", id).findFirst().getTitle();
    }

    public static Event getUpcomingEventById(String id, Realm realm) {
        Date today = new Date();
        return realm.where(Event.class).equalTo("id", id).greaterThanOrEqualTo("endDateTime", today).findFirst();
    }

    public static Event getEvent(String id, Realm realm) {
        return realm.where(Event.class).equalTo("id", id).findFirst();
    }

    public static RealmResults<Event> getUpFavEvents(Realm realm) {
        Date today = new Date();
        return realm.where(Event.class).equalTo("isFavourite", true).greaterThanOrEqualTo("endDateTime", today)
                .findAll();
    }

    public static RealmResults<Event> getUpcomingEvents(Realm realm) {
        Date today = new Date();
        return realm.where(Event.class).greaterThanOrEqualTo("endDateTime", today)
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static RealmResults<Event> getUpcomingRegisteredEvents(Realm realm, String userId) {
        Date today = new Date();
        return realm.where(Event.class).equalTo("isBookedLocal", true).greaterThanOrEqualTo("endDateTime", today)
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static Event checkIfEventIsPassed(Realm realm, String eventId) {
        Date today = new Date();
        return realm.where(Event.class).equalTo("id", eventId).lessThanOrEqualTo("endDateTime", today)
                .findFirst();
    }

    public static RealmResults<Event> getPastEvents(Realm realm) {
        Date today = new Date();
        return realm.where(Event.class).lessThan("endDateTime", today)
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static RealmResults<Event> getSurveyEvents(Realm realm) {
        Date today = new Date();
        return realm.where(Event.class).lessThanOrEqualTo("startDate", today).isNotNull("surveyId").isNotEmpty("surveyId")
                .findAll().sort("startDate", Sort.DESCENDING);
    }

    public static void fetchAllEvents(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchAllEvents URL =" + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(MMAConstants.TAG, response.toString());

                        realm.beginTransaction();
                        Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
                        Type collectionType = new TypeToken<ArrayList<Event>>() {
                        }.getType();

                        ArrayList<Event> eventList = gson.fromJson(response.toString(), collectionType);
                        if (eventList.size() > 0)
                            makeAllObsolete(realm);

                        for (Event event : eventList) {
                            SimpleDateFormat simpleDate = new SimpleDateFormat("MM/dd/yyyy");
                            String date = simpleDate.format(event.getEndDate());
                            String dateTime = date + "T" + event.getEndTime() + ":00";
                            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy'T'HH:mm:ss");
                            try {
                                Date timeObj = formatter.parse(dateTime);
                                event.setEndDateTime(timeObj);
                                Log.i(MMAConstants.TAG, "timeObj =" + timeObj.getTime());
                            } catch (ParseException e) {
                                event.setEndDateTime(event.getEndDate());
                                e.printStackTrace();
                            }
                            Event localEvent = getEvent(event.getId(), realm);
                            if (localEvent != null) {
                                event.setFavourite(localEvent.getFavourite());
                                event.setObsolete(false);
                            }

                            realm.copyToRealmOrUpdate(event);
                        }
                        deleteAllObsolete(realm);
                        realm.commitTransaction();
                        RealmResults events = query.findAll();
                        try {
                            callBack.fetchDidSucceed(events);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // }
                        // });
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

    public static void fetchUserEvents(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(MMAConstants.TAG, "fetchUserEvents URL =" + url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
                        try {

                            Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();

                            RealmResults users = query.findAll();

                            Type collectionType = new TypeToken<ArrayList<UserEventBO>>() {
                            }.getType();

                            ArrayList<UserEventBO> userEvents = gson.fromJson(response.toString(), collectionType);
                            String jsonString = gson.toJson(userEvents);

                            for (UserEventBO event : userEvents) {
                                Log.i(MMAConstants.TAG, "Date Created =     " + event.datecreated.toString());
                            }

                            callBack.fetchDidSucceed(users);
                        } catch (Exception e) {
                            Log.i(MMAConstants.TAG, "Exception Error - " + e.getMessage());
                            callBack.fetchDidFail(e);
                        }
                        //  }
                        //});
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
        Log.i(MMAConstants.TAG, "performUserRegistration =" + url);
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
        MMAApplication.getInstance().addToRequestQueue(rsvpRegistration);
    }

    public static void makeAllObsolete(Realm realm) {
        RealmResults allItems = realm.where(Event.class).findAll();
        for (int i = 0; i < allItems.size(); i++) {
            Event item = (Event) allItems.get(i);
            item.setObsolete(true);
        }
    }

    public static void deleteAllObsolete(Realm realm) {
        RealmResults obsoleteItems = realm.where(Event.class).equalTo("isObsolete", true).findAll();
        for (int i = 0; i < obsoleteItems.size(); i++) {
            Event item = (Event) obsoleteItems.get(i);
            item.deleteFromRealm();
        }
    }
}

package com.od.hrdf.API;

import com.od.hrdf.Utils.HRDFConstants;

import java.io.UnsupportedEncodingException;

/**
 * Created by Awais on 10/12/2016.
 */

public class Api {
    private static String baseURL = "http://www.mypams.net";
    private static String jogetAppId = "hrdfApp";
    private static String jogetAppVersion = "1";
    private static String jogetUserViewID = "hrdfAppUV";
    private static String joget_app_id = "hrdfApp";

    //METHODS
    public static String urlReverseGeoCoding(String address) throws UnsupportedEncodingException {
        String strURL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true";
        return strURL;
    }

    public static String urlJogetCRUD() {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", "www.mypams.net", "org.od.webservice.JsonCudApiPlugin2");
        return builder.build().toString();
    }

    public static String getAbsoluteURL(String strURL) {
        if (!strURL.startsWith(baseURL))
            return baseURL + strURL;

        return strURL;
    }

    public static String urlAboutHRDF() {
        return urlGetJogetList(HRDFConstants.AboutHRDFList);
    }

    public static String urlDocumentIds(String speaker) {
        return urlGetJogetList(HRDFConstants.DocumentIdsList, "speaker", speaker);
    }

    public static String urlDocumentList(String id) {
        return urlGetJogetList(HRDFConstants.DocumentsList, "documentId", id, true);
    }

    public static String urlArticleList() {
        return urlGetJogetList(HRDFConstants.ArticleList);
    }

    public static String urlUserList(String userId) {
        return urlGetJogetList(HRDFConstants.UserListId, "id", userId);
    }

    public static String urlEventList() {
        return urlGetJogetList(HRDFConstants.EventListId);
    }

    public static String urlUserEventList(String userId) {
        return urlGetJogetList(HRDFConstants.UserEventsList, "user", userId);
    }

    public static String urlEventAgenda(String eventId) {
        return urlGetJogetList(HRDFConstants.AgendaList, "event", eventId);
    }

    public static String urlEventFloorplan(String eventId) {
        return urlGetJogetList(HRDFConstants.FloorplanList, "event", eventId);
    }

    public static String urlSpeakerList() {
        return urlGetJogetList(HRDFConstants.SpeakerList);
    }

    public static String urlEventSpeakerList(String eventId) {
        return urlGetJogetList(HRDFConstants.EventSpeakerList, "event", eventId);
    }

    public static String urlSponsor(String sponsorId) {
        return urlGetJogetList(HRDFConstants.SponsorsList, "id", sponsorId);
    }

    public static String urlAllSponsor() {
        return urlGetJogetList(HRDFConstants.SponsorsList);
    }

    public static String urlEventSponsorsList(String eventId) {
        return urlGetJogetList(HRDFConstants.EventSponsorsList, "event", eventId);
    }

    public static String urlExhibitor(String exhibitorId) {
        return urlGetJogetList(HRDFConstants.ExhibitorsList, "id", exhibitorId);
    }

    public static String urlEventExhibitorsList(String eventId) {
        return urlGetJogetList(HRDFConstants.EventExhibitorsList, "event", eventId);
    }

    public static String urlEventTopicList(String eventId) {
       return urlGetJogetList(HRDFConstants.EventSpeakerTopicList, "event", eventId);
    }

    public static String urlEventSpeakerTopic(String eventId,String speakerId) {
        return urlGetJogetList(HRDFConstants.EventSpeakerTopicList, "event", eventId, "speaker", speakerId);
    }

    public static String urlGetJogetList(String listId) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", "www.mypams.net", "org.od.webservice.JsonApiPlugin2");
        builder.appId(HRDFConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.willIncludeImages(true);
        return builder.build().toString();
    }

    public static String urlGetJogetList(String listId, String filter1name, String filter1Value) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", "www.mypams.net", "org.od.webservice.JsonApiPlugin2");
        builder.appId(HRDFConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.primaryFilter(filter1name, filter1Value);
        builder.willIncludeImages(true);
        return builder.build().toString();
    }

    public static String urlGetJogetList(String listId, String filter1name, String filter1Value, String filter2name, String filter2Value) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", "www.mypams.net", "org.od.webservice.JsonApiPlugin2");
        builder.appId(HRDFConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.primaryFilter(filter1name, filter1Value);
        builder.secondaryFilter(filter2name,filter2Value);
        builder.willIncludeImages(true);
        return builder.build().toString();
    }

    public static String urlGetJogetList(String listId, String filter1name, String filter1Value, boolean fileUrl) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", "www.mypams.net", "org.od.webservice.JsonApiPlugin2");
        builder.appId(HRDFConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.primaryFilter(filter1name, filter1Value);
        builder.willIncludeImages(true);
        builder.willIncludeFiles(fileUrl);
        return builder.build().toString();
    }

    public static String urlGetJogetList(String listId, String filter1name, String filter1Value, boolean imageUrl, boolean fileUrl) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", "www.mypams.net", "org.od.webservice.JsonApiPlugin2");
        builder.appId(HRDFConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.primaryFilter(filter1name, filter1Value);
        builder.willIncludeImages(imageUrl);
        builder.willIncludeFiles(fileUrl);
        return builder.build().toString();
    }
}
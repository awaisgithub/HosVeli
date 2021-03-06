package com.od.mma.API;

import com.od.mma.BOs.User;
import com.od.mma.Utils.MMAConstants;

import java.io.UnsupportedEncodingException;

/**
 * Created by Awais on 10/12/2016.
 */

public class Api {
    public static String basicBaseURL = "103.233.0.208";

    //public static String basicBaseURL = "www.mypams.net";
    public static String MMABaseUrl = "104.155.207.137";
    //private static String baseURL = "http://www.mypams.net";
    private static String baseURL = "http://103.233.0.208";
    private static String jogetAppId = "hrdfApp";
    private static String jogetAppVersion = "1";
    private static String jogetUserViewID = "hrdfAppUV";
    private static String joget_app_id = "hrdfApp";


    //MMA

    public static String urlSignUp(User signup_user, String gcm_id, String device_type) {
        String signUpUrl = "http://" + MMABaseUrl + "/jw/web/json/plugin/org.joget.mmaWebServices.webservices/service?";
        signUpUrl = signUpUrl + "id=" + signup_user.getEmail();
        signUpUrl = signUpUrl + "&username=" + signup_user.getEmail();
        signUpUrl = signUpUrl + "&password=" + signup_user.getApplicantPassword();
        signUpUrl = signUpUrl + "&firstName=" + signup_user.getFname();
        signUpUrl = signUpUrl + "&lastName=" + signup_user.getLname();
        signUpUrl = signUpUrl + "&email=" + signup_user.getEmail();
        signUpUrl = signUpUrl + "&gcmId=" + "1";
        signUpUrl = signUpUrl + "&deviceType=" + device_type;
        signUpUrl = signUpUrl + "&method=" + MMAConstants.method_create_user;
        return signUpUrl;
    }

    public static String urlLogin(String email, String password) {
        String loginUrl = "http://" + MMABaseUrl + "/jw/web/json/plugin/org.joget.mmaWebServices.webservices/service?";
        loginUrl = loginUrl + "&username=" + email;
        loginUrl = loginUrl + "&password=" + password;
        loginUrl = loginUrl + "&method=" + MMAConstants.method_login_user;
        return loginUrl;
    }

    public static String urlForgotPassword(String email) {
        String forgotUrl = "http://" + MMABaseUrl + "/jw/web/json/plugin/org.joget.mmaWebServices.webservices/service?";
        forgotUrl = forgotUrl + "&email=" + email;
        forgotUrl = forgotUrl + "&method=" + MMAConstants.method_forget_pass;
        return forgotUrl;
    }

    public static String urlMembershipUpdate() {
        String membershipUrl = "http://" + MMABaseUrl + "/jw/web/json/plugin/org.joget.mmaWebServices.webservices/service?method=cud";
        return membershipUrl;
    }

    public static String urlReadApplicant(String email) {
        String readUrl = "http://" + MMABaseUrl + "/jw/web/json/plugin/org.joget.mmaWebServices.webservices/service?";
        readUrl = readUrl + "table=" + MMAConstants.ApplicantTable;
        readUrl = readUrl + "&filterColumn=id" + "&filterValue=" + email;
        readUrl = readUrl + "&method=" + MMAConstants.method_read_SQL;
        return readUrl;
    }

    public static String urlDataListData(String listId) {
        String datalistUrl = "http://" + MMABaseUrl + "/jw/web/json/plugin/org.joget.mmaWebServices.webservices/service?";
        datalistUrl = datalistUrl + "listId=" + listId;
        datalistUrl = datalistUrl + "&action=" + MMAConstants.action_list;
        datalistUrl = datalistUrl + "&appId=" + MMAConstants.app_id;
        datalistUrl = datalistUrl + "&filter1Column=&filter1Value=&method=listDataList";
        return datalistUrl;
    }

    public static String urlApplicantData(String email) {
        String datalistUrl = "http://" + MMABaseUrl + "/jw/web/json/plugin/org.joget.mmaWebServices.webservices/service?";
        datalistUrl = datalistUrl + "listId=" + "pi";
        datalistUrl = datalistUrl + "&action=" + MMAConstants.action_list;
        datalistUrl = datalistUrl + "&appId=" + MMAConstants.app_id;
        datalistUrl = datalistUrl + "&filter1Column=id" + "&filter1Value=" + email;
        datalistUrl = datalistUrl + "&method=listDataList";
        datalistUrl = datalistUrl + "&imageUrl=" + "Yes";
        return datalistUrl;
    }
    //
















    //METHODS
    public static String urlReverseGeoCoding(String address) throws UnsupportedEncodingException {
        String strURL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true";
        return strURL;
    }

    public static String urlJogetCRUD() {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", basicBaseURL, "org.od.webservice.JsonCudApiPlugin2");
        return builder.build().toString();
    }

    public static String getAbsoluteURL(String strURL) {
        if (!strURL.startsWith(baseURL))
            return baseURL + strURL;

        return strURL;
    }

    public static String urlAboutHRDF() {
        return urlGetJogetList(MMAConstants.AboutHRDFList);
    }

    public static String urlDocumentIds(String speaker) {
        return urlGetJogetList(MMAConstants.DocumentIdsList, "speaker", speaker);
    }

    public static String urlDocumentList(String id) {
        return urlGetJogetList(MMAConstants.DocumentsList, "documentId", id, true);
    }

    public static String urlArticleList() {
        return urlGetJogetList(MMAConstants.ArticleList);
    }

    public static String urlArticleById(String articleId) {
        return urlGetJogetList(MMAConstants.ArticleList, "id", articleId, true);
    }

    public static String urlUserList(String userId) {
        return urlGetJogetList(MMAConstants.UserListId, "id", userId);
    }

    public static String urlEventList() {
        return urlGetJogetList(MMAConstants.EventListId);
    }

    public static String urlEventById(String eventId) {

        return urlGetJogetList(MMAConstants.EventListId, "id", eventId, true);
    }

    public static String urlUserEventList(String userId) {
        return urlGetJogetList(MMAConstants.UserEventsList, "user", userId);
    }

    public static String urlEventAgenda(String eventId) {
        return urlGetJogetList(MMAConstants.AgendaList, "event", eventId);
    }

    public static String urlBranches() {
        return urlGetJogetList(MMAConstants.BranchList);
    }

    public static String urlEventFloorplan(String eventId) {
        return urlGetJogetList(MMAConstants.FloorplanList, "event", eventId);
    }

    public static String urlSpeakerList() {
        return urlGetJogetList(MMAConstants.SpeakerList);
    }

    public static String urlEventSpeakerList(String eventId) {
        return urlGetJogetList(MMAConstants.EventSpeakerList, "event", eventId);
    }

    public static String urlSponsor(String sponsorId) {
        return urlGetJogetList(MMAConstants.SponsorsList, "id", sponsorId);
    }

    public static String urlAllSponsor() {
        return urlGetJogetList(MMAConstants.SponsorsList);
    }

    public static String urlEventSponsorsList(String eventId) {
        return urlGetJogetList(MMAConstants.EventSponsorsList, "event", eventId);
    }

    public static String urlSubmitUserRating() {
        return "http://" + basicBaseURL + "/jw/web/json/plugin/org.joget.hrdf.eventSpeakerRatingWS/service";
    }

    public static String urlExhibitor(String exhibitorId) {
        return urlGetJogetList(MMAConstants.ExhibitorsList, "id", exhibitorId);
    }

    public static String urlAllExhibitor() {
        return urlGetJogetList(MMAConstants.ExhibitorsList);
    }

    public static String urlGCMList(String userId) {
        return urlGetJogetList(MMAConstants.USERGCMList, "user", userId);
    }

    public static String urlEventExhibitorsList(String eventId) {
        return urlGetJogetList(MMAConstants.EventExhibitorsList, "event", eventId);
    }

    public static String urlEventTopicList(String eventId) {
        return urlGetJogetList(MMAConstants.EventSpeakerTopicList, "event", eventId);
    }

    public static String urlEventSpeakerTopic(String eventId, String speakerId) {
        return urlGetJogetList(MMAConstants.EventSpeakerTopicList, "event", eventId, "speaker", speakerId);
    }

    public static String urlGetJogetList(String listId) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", basicBaseURL, "org.od.webservice.JsonApiPlugin2");
        builder.appId(MMAConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.willIncludeImages(true);
        return builder.build().toString();
    }

    public static String urlGetJogetList(String listId, String filter1name, String filter1Value) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", basicBaseURL, "org.od.webservice.JsonApiPlugin2");
        builder.appId(MMAConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.primaryFilter(filter1name, filter1Value);
        builder.willIncludeImages(true);
        return builder.build().toString();
    }

    public static String urlGetJogetList(String listId, String filter1name, String filter1Value, String filter2name, String filter2Value) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", basicBaseURL, "org.od.webservice.JsonApiPlugin2");
        builder.appId(MMAConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.primaryFilter(filter1name, filter1Value);
        builder.secondaryFilter(filter2name, filter2Value);
        builder.willIncludeImages(true);
        return builder.build().toString();
    }

    public static String urlGetJogetList(String listId, String filter1name, String filter1Value, boolean fileUrl) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", basicBaseURL, "org.od.webservice.JsonApiPlugin2");
        builder.appId(MMAConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.primaryFilter(filter1name, filter1Value);
        builder.willIncludeImages(true);
        builder.willIncludeFiles(fileUrl);
        return builder.build().toString();
    }

    public static String urlGetJogetList(String listId, String filter1name, String filter1Value, boolean imageUrl, boolean fileUrl) {
        JogetAPIBuilder builder = new JogetAPIBuilder("http", basicBaseURL, "org.od.webservice.JsonApiPlugin2");
        builder.appId(MMAConstants.JOGET_APP_ID);
        builder.listId(listId);
        builder.action("list");
        builder.primaryFilter(filter1name, filter1Value);
        builder.willIncludeImages(imageUrl);
        builder.willIncludeFiles(fileUrl);
        return builder.build().toString();
    }
}
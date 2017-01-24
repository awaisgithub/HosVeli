package com.od.mma.Payload;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.od.mma.BOs.User;
import com.od.mma.Membership.Membership;
import com.od.mma.Utils.MMAConstants;
import com.od.mma.event.speaker.SpeakerRateDialogFrag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Mahmoor on 15/6/2016.
 */
public class JSONPayloadManager {
    private static JSONPayloadManager ourInstance = new JSONPayloadManager();

    private JSONPayloadManager() {
    }

    public static JSONPayloadManager getInstance() {
        return ourInstance;
    }

    //MMA

    public JSONObject getMembershipPayload(User user, Membership membership) throws JSONException {
        RegPayloadBO regPayloadBOUpdate = new RegPayloadBO();
        regPayloadBOUpdate.setOperation(MMAConstants.DB_OP_UPDATE);
        regPayloadBOUpdate.setTable_name(MMAConstants.USER_TABLE);

        ArrayList<DataBO> keyList = regPayloadBOUpdate.getKey();
        keyList.add(new DataBO("id", user.getId()));

        ArrayList<DataBO> dataList = regPayloadBOUpdate.getData();
        //1st
        dataList.add(new DataBO("membershipCategory", membership.getMembershipCategory()));
        if (membership.getMembershipCategory().equals("Medical Officer Membership")) {
            dataList.add(new DataBO("yearOfService", membership.getYearOfService()));
        } else {
            dataList.add(new DataBO("yearOfService", ""));
        }
        //2nd
        dataList.add(new DataBO("email", user.getId())); //hidden
        dataList.add(new DataBO("titleSelectionBox", membership.getTitleSelectionBox()));
        dataList.add(new DataBO("applicantFirstName", membership.getApplicantFirstName()));
        dataList.add(new DataBO("applicantLastName", membership.getApplicantLastName()));
        dataList.add(new DataBO("applicantDateOfBirth", membership.getApplicantDateOfBirth()));
        dataList.add(new DataBO("applicantGender", membership.getApplicantGender()));
        dataList.add(new DataBO("applicantMaritalStatus", membership.getApplicantMaritalStatus()));
        if (membership.getPersonal_pic() != null) {
            dataList.add(new DataBO("applicantPicture", "applicantPicture.jpeg"));
        }
        //3rd
        dataList.add(new DataBO("identificationType", membership.getIdentificationType()));
        dataList.add(new DataBO("nricNew", membership.getNricNew()));
        dataList.add(new DataBO("applicantNationality", membership.getApplicantNationality()));
        dataList.add(new DataBO("applicantRace", membership.getApplicantRace()));
        dataList.add(new DataBO("applicantReligion", membership.getApplicantReligion()));
        if (membership.getNric_pic() != null) {
            dataList.add(new DataBO("applicantNRICPic", "applicantNRICPic.jpeg"));
        }
        //4th
        dataList.add(new DataBO("country", membership.getCountry()));
        dataList.add(new DataBO("state", membership.getState()));
        dataList.add(new DataBO("registrationState", membership.getRegistrationState()));
        dataList.add(new DataBO("city", membership.getCity()));
        dataList.add(new DataBO("postCode", membership.getPostCode()));
        dataList.add(new DataBO("address", membership.getAddress()));
        dataList.add(new DataBO("telephoneNo", membership.getTelephoneNo()));
        //5th
        dataList.add(new DataBO("countryResidential", membership.getCountryResidential()));
        dataList.add(new DataBO("stateResidential", membership.getStateResidential()));
        dataList.add(new DataBO("city_1", membership.getCity_1()));
        dataList.add(new DataBO("postCodeResidential", membership.getPostCodeResidential()));
        dataList.add(new DataBO("addressResidential", membership.getAddressResidential()));
        dataList.add(new DataBO("telephoneNoResidential", membership.getTelephoneNoResidential()));
        dataList.add(new DataBO("correspondenceSelection", membership.getCorrespondenceSelection()));
        //6th
        dataList.add(new DataBO("isJointAccount", membership.getIsJointAccount()));
        dataList.add(new DataBO("spouseIdentificationType", membership.getSpouseIdentificationType()));
        dataList.add(new DataBO("spouseNRICNew", membership.getSpouseNRICNew()));
        dataList.add(new DataBO("applicantSpouseFirstName", membership.getApplicantSpouseFirstName()));
        dataList.add(new DataBO("applicantSpouseUsername", membership.getApplicantSpouseUsername()));
        //7th
        JSONObject basic = new JSONObject();
        basic.put("", "");
        basic.put("dateOfQualification", membership.getBachelor_qualification_date());
        basic.put("country", membership.getDegree_bachelor_country());
        basic.put("basicDegreeName", membership.getDegree_bachelor());
        basic.put("university", membership.getBachelor_uni());
        basic.put("id", UUID.randomUUID().toString());
        JsonArray basic_degree = new JsonArray();
        basic_degree.add(String.valueOf(basic));
        dataList.add(new DataBO("basicDegreeInfo", basic_degree.toString()));
        //8th
        switch (membership.getPos_count()) {
            case 1:
                JSONObject pos_degree_1 = new JSONObject();
                pos_degree_1.put("", "");
                pos_degree_1.put("dateOfQualification", membership.getPos_qof_date());
                pos_degree_1.put("country", membership.getPos_country());
                pos_degree_1.put("basicDegreeName", membership.getPos_degree());
                pos_degree_1.put("university", membership.getPos_uni());
                pos_degree_1.put("id", UUID.randomUUID().toString());
                JsonArray pos_degree_final_1 = new JsonArray();
                pos_degree_final_1.add(String.valueOf(pos_degree_1));
                dataList.add(new DataBO("postgraduateDegreeInfo", pos_degree_final_1.toString()));
                break;
            case 2:
                JSONObject pos_degree_2_1 = new JSONObject();
                pos_degree_2_1.put("", "");
                pos_degree_2_1.put("dateOfQualification", membership.getPos_qof_date());
                pos_degree_2_1.put("country", membership.getPos_country());
                pos_degree_2_1.put("basicDegreeName", membership.getPos_degree());
                pos_degree_2_1.put("university", membership.getPos_uni());
                pos_degree_2_1.put("id", UUID.randomUUID().toString());
                JSONObject pos_degree_2_2 = new JSONObject();
                pos_degree_2_2.put("", "");
                pos_degree_2_2.put("dateOfQualification", membership.getPos_qof_date1());
                pos_degree_2_2.put("country", membership.getPos_country1());
                pos_degree_2_2.put("basicDegreeName", membership.getPos_degree1());
                pos_degree_2_2.put("university", membership.getPos_uni1());
                pos_degree_2_2.put("id", UUID.randomUUID().toString());
                JsonArray pos_degree_final_2 = new JsonArray();
                pos_degree_final_2.add(String.valueOf(pos_degree_2_1));
                pos_degree_final_2.add(String.valueOf(pos_degree_2_2));
                dataList.add(new DataBO("postgraduateDegreeInfo", pos_degree_final_2.toString()));
                break;
            case 3:
                JSONObject pos_degree_3_1 = new JSONObject();
                pos_degree_3_1.put("", "");
                pos_degree_3_1.put("dateOfQualification", membership.getPos_qof_date());
                pos_degree_3_1.put("country", membership.getPos_country());
                pos_degree_3_1.put("basicDegreeName", membership.getPos_degree());
                pos_degree_3_1.put("university", membership.getPos_uni());
                pos_degree_3_1.put("id", UUID.randomUUID().toString());
                JSONObject pos_degree_3_2 = new JSONObject();
                pos_degree_3_2.put("", "");
                pos_degree_3_2.put("dateOfQualification", membership.getPos_qof_date1());
                pos_degree_3_2.put("country", membership.getPos_country1());
                pos_degree_3_2.put("basicDegreeName", membership.getPos_degree1());
                pos_degree_3_2.put("university", membership.getPos_uni1());
                pos_degree_3_2.put("id", UUID.randomUUID().toString());
                JSONObject pos_degree_3_3 = new JSONObject();
                pos_degree_3_3.put("", "");
                pos_degree_3_3.put("dateOfQualification", membership.getPos_qof_date2());
                pos_degree_3_3.put("country", membership.getPos_country2());
                pos_degree_3_3.put("basicDegreeName", membership.getPos_degree2());
                pos_degree_3_3.put("university", membership.getPos_uni2());
                pos_degree_3_3.put("id", UUID.randomUUID().toString());
                JsonArray pos_degree_final_3 = new JsonArray();
                pos_degree_final_3.add(String.valueOf(pos_degree_3_1));
                pos_degree_final_3.add(String.valueOf(pos_degree_3_2));
                pos_degree_final_3.add(String.valueOf(pos_degree_3_3));
                dataList.add(new DataBO("postgraduateDegreeInfo", pos_degree_final_3.toString()));
                break;
        }
        //9th
        if (membership.getMain_category().equals("Student")) {
            dataList.add(new DataBO("studentMemberUniversity", membership.getStudentMemberUniversity()));
            dataList.add(new DataBO("studentMemberStudyCompletionYear", membership.getStudentMemberStudyCompletionYear()));
        } else {
            dataList.add(new DataBO("applicantMMCRegistrationNo", membership.getApplicantMMCRegistrationNo()));
            dataList.add(new DataBO("tpcRegistrationNo", membership.getTpcRegistrationNo()));
            dataList.add(new DataBO("dateOfRegistrationMMC", membership.getDateOfRegistrationMMC()));
            if (membership.getMembershipCategory().equalsIgnoreCase("Student")) {
                if (membership.getUni_student_card() != null) {
                    dataList.add(new DataBO("studentMemberUniversityLetter", "studentMemberUniversityLetter.jpeg"));
                }
            } else {
                if (membership.getMmc_certificate() != null) {
                    dataList.add(new DataBO("applicantMMCRegistrationCopy", "applicantMMCRegistrationCopy.jpeg"));
                }
            }
        }
        //10th
        dataList.add(new DataBO("employmentStatusSelectBox", membership.getEmploymentStatusSelectBox()));
        dataList.add(new DataBO("practiceNature", membership.getPracticeNature()));
        dataList.add(new DataBO("practiceNatureSubCategory", membership.getPracticeNatureSubCategory()));
        //images
        ArrayList<DataImageBO> imgList = regPayloadBOUpdate.getImageArray();
        if (membership.getPersonal_pic() != null) {
            imgList.add(new DataImageBO("applicantPicture.jpeg", membership.getApplicantPicture()));
        }
        if (membership.getNric_pic() != null) {
            imgList.add(new DataImageBO("applicantNRICPic.jpeg", membership.getApplicantNRICPic()));
        }
        if (membership.getMembershipCategory().equalsIgnoreCase("Student")) {
            if (membership.getUni_student_card() != null) {
                imgList.add(new DataImageBO("studentMemberUniversityLetter.jpeg", membership.getStudentMemberUniversityLetter()));
            }
        } else {
            if (membership.getMmc_certificate() != null) {
                imgList.add(new DataImageBO("applicantMMCRegistrationCopy.jpeg", membership.getApplicantMMCRegistrationCopy()));
            }
        }

        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBOUpdate);
        Log.i(MMAConstants.TAG_MMA, "MEMBERSHIP JSON(request) =" + requestJSON);
        JSONObject regInfoJSON = null;
        try {
            regInfoJSON = new JSONObject(requestJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        int maxLogSize = 1000;
//        for (int i = 0; i <= regInfoJSON.toString().length() / maxLogSize; i++) {
//            int start = i * maxLogSize;
//            int end = (i + 1) * maxLogSize;
//            end = end > regInfoJSON.toString().length() ? regInfoJSON.toString().length() : end;
//            Log.i(MMAConstants.TAG_MMA, "MEMBERSHIP JSON LONG STRING = " + regInfoJSON.toString().substring(start, end));
//        }
        Log.i("MMAConstants.TAG_MMA", "MEMBERSHIP JSON= " + regInfoJSON.toString());
        return regInfoJSON;
    }

    //


    public JSONObject getRegReqPayload(User user) {

        RegPayloadBO regPayloadBO = new RegPayloadBO();
        regPayloadBO.setOperation(MMAConstants.DB_OP_CREATE);
        regPayloadBO.setTable_name(MMAConstants.USER_REG_TABLE);
        ArrayList<DataBO> dataList = regPayloadBO.getData();
        dataList.add(new DataBO("name", user.getName()));
        dataList.add(new DataBO("id", user.getId()));
        dataList.add(new DataBO("password", user.getApplicantPassword()));
        dataList.add(new DataBO("passwd", user.getApplicantPassword()));
        dataList.add(new DataBO("contactNumber", user.getContactNumber()));
        dataList.add(new DataBO("nationality", user.getNationality()));
        dataList.add(new DataBO("type", "User"));
        dataList.add(new DataBO("designation", user.getDesignation()));
        dataList.add(new DataBO("company", user.getCompany()));


        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBO);
        Log.i(MMAConstants.TAG, "requestJSON =" + requestJSON);
        JSONObject regInfoJSON = null;
        try {
            regInfoJSON = new JSONObject(requestJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return regInfoJSON;
    }

    public JSONObject getRegReqPayloadUpdation(User user) {

        RegPayloadBO regPayloadBOUpdate = new RegPayloadBO();
        regPayloadBOUpdate.setOperation(MMAConstants.DB_OP_UPDATE);
        regPayloadBOUpdate.setTable_name(MMAConstants.USER_REG_TABLE);
        ArrayList<DataBO> dataList = regPayloadBOUpdate.getData();
        dataList.add(new DataBO("name", user.getName()));
        dataList.add(new DataBO("contactNumber", user.getContactNumber()));
        dataList.add(new DataBO("nationality", user.getName()));
        dataList.add(new DataBO("designation", user.getDesignation()));
        dataList.add(new DataBO("company", user.getCompany()));
        ArrayList<DataBO> keyList = regPayloadBOUpdate.getKey();
        keyList.add(new DataBO("id", user.getId()));

        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBOUpdate);
        Log.i(MMAConstants.TAG, "requestJSON =" + requestJSON);
        JSONObject regInfoJSON = null;
        try {
            regInfoJSON = new JSONObject(requestJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("AWAIS1", "JSON= " + regInfoJSON.toString());
        return regInfoJSON;
    }

    public JSONObject getRSVPReqPayload(String eventId, String userId) {

        RegPayloadBO regPayloadBO = new RegPayloadBO();
        regPayloadBO.setOperation(MMAConstants.DB_OP_CREATE);
        regPayloadBO.setTable_name(MMAConstants.USER_RSVP_TABLE);
        regPayloadBO.setKey(null);
        ArrayList<DataBO> dataList = regPayloadBO.getData();
        dataList.add(new DataBO("event", eventId));
        dataList.add(new DataBO("user", userId));
        dataList.add(new DataBO("rsvp", "Book"));

        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBO);
        Log.i(MMAConstants.TAG, "requestJSON =" + requestJSON);
        JSONObject regInfoJSON = null;
        try {
            regInfoJSON = new JSONObject(requestJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return regInfoJSON;
    }

    public JSONObject getGCMUploadPayload(String userId, String gcmToken) {

        RegPayloadBO regPayloadBO = new RegPayloadBO();
        regPayloadBO.setOperation(MMAConstants.DB_OP_CREATE);
        regPayloadBO.setTable_name(MMAConstants.GCM_REG_TABLE);
        regPayloadBO.setKey(null);
        ArrayList<DataBO> dataList = regPayloadBO.getData();
        dataList.add(new DataBO("user", userId));
        dataList.add(new DataBO("deviceType", "Android"));
        dataList.add(new DataBO("token", gcmToken));

        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBO);
        JSONObject regInfoJSON = null;
        try {
            regInfoJSON = new JSONObject(requestJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return regInfoJSON;
    }

    public JSONObject getUpdateGCMIDPayload(String userId, String gcmToken) {

        RegPayloadBO regPayloadBOUpdate = new RegPayloadBO();
        regPayloadBOUpdate.setOperation(MMAConstants.DB_OP_UPDATE);
        regPayloadBOUpdate.setTable_name(MMAConstants.GCM_REG_TABLE);
        ArrayList<DataBO> dataList = regPayloadBOUpdate.getData();
        dataList.add(new DataBO("token", gcmToken));
        dataList.add(new DataBO("deviceType", "Android"));
        ArrayList<DataBO> keyList = regPayloadBOUpdate.getKey();
        keyList.add(new DataBO("user", userId));

        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBOUpdate);
        Log.i(MMAConstants.TAG, "requestJSON getUpdateGCMIDPayload=" + requestJSON);
        JSONObject regInfoJSON = null;
        try {
            regInfoJSON = new JSONObject(requestJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("AWAIS1", "JSON= " + regInfoJSON.toString());
        return regInfoJSON;
    }

    public JSONObject getSpeakerRatingPayload(SpeakerRateDialogFrag.SpeakerRatingBO ratingBO) {

        Gson gson = new Gson();
        String requestJSON = gson.toJson(ratingBO);
        Log.i(MMAConstants.TAG, "requestJSON =" + requestJSON);
        JSONObject regInfoJSON = null;
        try {
            regInfoJSON = new JSONObject(requestJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return regInfoJSON;
    }

    private class RegPayloadBO {
        String operation;
        String table_name;
        ArrayList<DataBO> data = new ArrayList<>();
        ArrayList<DataBO> key = new ArrayList<>();
        ArrayList<DataImageBO> imageArray = new ArrayList<>();

        public ArrayList<DataImageBO> getImageArray() {
            return imageArray;
        }

        public void setImageArray(ArrayList<DataImageBO> imageArray) {
            this.imageArray = imageArray;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public ArrayList<DataBO> getData() {
            return data;
        }

        public void setData(ArrayList<DataBO> data) {
            this.data = data;
        }

        public ArrayList<DataBO> getKey() {
            return key;
        }

        public void setKey(ArrayList<DataBO> keys) {
            this.key = keys;
        }
    }

    private class DataBO {
        String column;
        String name;
        String value;

        public DataBO(String column, String value) {
            this.column = column;
            this.value = value;
        }

        public DataBO(String column, String name, String value) {
            this.column = column;
            this.name = name;
            this.value = value;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private class DataImageBO {
        String imageName;
        String name;
        String imageBson;

        public DataImageBO(String column, String value) {
            this.imageName = column;
            this.imageBson = value;
        }

        public DataImageBO(String column, String name, String value) {
            this.imageName = column;
            this.name = name;
            this.imageBson = value;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageBson() {
            return imageBson;
        }

        public void setImageBson(String imageBson) {
            this.imageBson = imageBson;
        }
    }

}

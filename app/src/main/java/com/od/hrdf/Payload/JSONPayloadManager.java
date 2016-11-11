package com.od.hrdf.Payload;

import android.util.Log;

import com.google.gson.Gson;
import com.od.hrdf.BOs.User;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.event.speaker.SpeakerRateDialogFrag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public JSONObject getRegReqPayload(User user) {

        RegPayloadBO regPayloadBO = new RegPayloadBO();
        regPayloadBO.setOperation(HRDFConstants.DB_OP_CREATE);
        regPayloadBO.setTable_name(HRDFConstants.USER_REG_TABLE);
        ArrayList dataList = regPayloadBO.getData();
        dataList.add(new DataBO("name", user.getName()));
        dataList.add(new DataBO("id", user.getId()));
        dataList.add(new DataBO("password", user.getPassword()));
        dataList.add(new DataBO("passwd", user.getPassword()));
        dataList.add(new DataBO("contactNumber", user.getContactNumber()));
        dataList.add(new DataBO("nationality", user.getName()));
        dataList.add(new DataBO("type", "User"));
        dataList.add(new DataBO("designation", user.getDesignation()));
        dataList.add(new DataBO("company", user.getCompany()));


        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBO);
        Log.i(HRDFConstants.TAG, "requestJSON =" + requestJSON);
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
        regPayloadBOUpdate.setOperation(HRDFConstants.DB_OP_UPDATE);
        regPayloadBOUpdate.setTable_name(HRDFConstants.USER_REG_TABLE);
        ArrayList dataList = regPayloadBOUpdate.getData();
        dataList.add(new DataBO("name", user.getName()));
        dataList.add(new DataBO("contactNumber", user.getContactNumber()));
        dataList.add(new DataBO("nationality", user.getName()));
        dataList.add(new DataBO("designation", user.getDesignation()));
        dataList.add(new DataBO("company", user.getCompany()));
        ArrayList keyList = regPayloadBOUpdate.getKey();
        keyList.add(new DataBO("id", user.getId()));

        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBOUpdate);
        Log.i(HRDFConstants.TAG, "requestJSON =" + requestJSON);
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
        regPayloadBO.setOperation(HRDFConstants.DB_OP_CREATE);
        regPayloadBO.setTable_name(HRDFConstants.USER_RSVP_TABLE);
        regPayloadBO.setKey(null);
        ArrayList dataList = regPayloadBO.getData();
        dataList.add(new DataBO("event", eventId));
        dataList.add(new DataBO("user", userId));
        dataList.add(new DataBO("rsvp", "Book"));

        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBO);
        Log.i(HRDFConstants.TAG, "requestJSON =" + requestJSON);
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
        regPayloadBO.setOperation(HRDFConstants.DB_OP_CREATE);
        regPayloadBO.setTable_name(HRDFConstants.GCM_REG_TABLE);
        regPayloadBO.setKey(null);
        ArrayList dataList = regPayloadBO.getData();
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
        regPayloadBOUpdate.setOperation(HRDFConstants.DB_OP_UPDATE);
        regPayloadBOUpdate.setTable_name(HRDFConstants.GCM_REG_TABLE);
        ArrayList dataList = regPayloadBOUpdate.getData();
        dataList.add(new DataBO("token", gcmToken));
        dataList.add(new DataBO("deviceType", "Android"));
        ArrayList keyList = regPayloadBOUpdate.getKey();
        keyList.add(new DataBO("user", userId));

        Gson gson = new Gson();
        String requestJSON = gson.toJson(regPayloadBOUpdate);
        Log.i(HRDFConstants.TAG, "requestJSON getUpdateGCMIDPayload=" + requestJSON);
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
        Log.i(HRDFConstants.TAG, "requestJSON =" + requestJSON);
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


}

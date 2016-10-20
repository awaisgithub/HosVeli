package com.od.hrdf.Payload;

import android.util.Log;

import com.google.gson.Gson;
import com.od.hrdf.BOs.User;
import com.od.hrdf.Utils.HRDFConstants;
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
        dataList.add(new DataBO("designation", ""));
        dataList.add(new DataBO("company", ""));


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

    private class RegPayloadBO {
        String operation;
        String table_name;
        ArrayList<DataBO> data = new ArrayList<>();
        ArrayList<DataBO> key = null;

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

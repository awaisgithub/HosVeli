package com.od.mma.CallBack;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by awais on 19/01/2017.
 */

public interface ServerReadCallBack {
    public void success(JSONArray response);

    public void failure(String response);
}

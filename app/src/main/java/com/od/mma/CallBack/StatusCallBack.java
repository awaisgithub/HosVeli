package com.od.mma.CallBack;

import org.json.JSONObject;

/**
 * Created by MuhammadMahmoor on 10/20/16.
 */

public interface StatusCallBack {

    public void success(JSONObject response);
    public void failure(String response);
}

package com.od.mma.CallBack;

import org.json.JSONObject;

/**
 * Created by MuhammadMahmoor on 10/20/16.
 */

public interface LoginCallBack {

    public void fetchDidSucceed(JSONObject response);
    public void fetchDidFail(Exception e);
}

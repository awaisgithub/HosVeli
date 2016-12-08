package com.od.mma.CallBack;

/**
 * Created by MuhammadMahmoor on 10/20/16.
 */

public interface LoginCallBack {

    public void fetchDidSucceed(String response);
    public void fetchDidFail(Exception e);
}

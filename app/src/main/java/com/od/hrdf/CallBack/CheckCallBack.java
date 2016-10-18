package com.od.hrdf.CallBack;

/**
 * Created by Awais on 10/17/2016.
 */

public interface CheckCallBack {
    public void checkDuplicateUser(boolean fetchedItems);

    public void checkFail(Exception e);

}

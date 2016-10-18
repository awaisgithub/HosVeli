package com.od.hrdf.CallBack;

import com.od.hrdf.BOs.Exhibitor;

/**
 * Created by Awais on 10/17/2016.
 */

public interface LocationCallBack {
    public void locationCordinates(String cordinates);

    public void locationFail(Exception e);
}

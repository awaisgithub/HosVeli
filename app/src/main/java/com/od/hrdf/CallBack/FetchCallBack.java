package com.od.hrdf.CallBack;

import android.location.Address;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Awais on 10/13/2016.
 */

public interface FetchCallBack {
    public void fetchDidSucceed(RealmResults fetchedItems);

    public void fetchDidFail(Exception e);

}

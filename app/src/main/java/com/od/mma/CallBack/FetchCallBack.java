package com.od.mma.CallBack;

import io.realm.RealmResults;

/**
 * Created by Awais on 10/13/2016.
 */

public interface FetchCallBack {
    public void fetchDidSucceed(RealmResults fetchedItems);
    public void fetchDidFail(Exception e);

}

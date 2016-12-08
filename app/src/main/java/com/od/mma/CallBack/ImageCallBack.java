package com.od.mma.CallBack;

import android.graphics.Bitmap;

/**
 * Created by Awais on 10/21/2016.
 */

public interface ImageCallBack {
    public void fetchImageSucceed(Bitmap image);
    public void fetchImageFail(String error);
}

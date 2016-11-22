package com.od.hrdf;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.od.hrdf.Utils.LollipopBitmapMemoryCacheParamsSupplier;
import com.od.hrdf.Utils.TypefaceUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Awais on 10/10/2016.
 */

public class HRDFApplication extends Application {

    private static HRDFApplication mInstance;
    private RequestQueue requestQueue;
    String newVar;
    public static Context context;
    public static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        mInstance = this;
        context = this;
//        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig
//                .newBuilder(getApplicationContext())
//                .setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier(activityManager))
//                .build();
        Fresco.initialize(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);

        requestQueue = Volley.newRequestQueue(this);
    }

    public static synchronized HRDFApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}

package com.od.hrdf;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
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
        Fresco.initialize(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);

        requestQueue = Volley.newRequestQueue(this);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "HelveticaNeue.ttf");
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

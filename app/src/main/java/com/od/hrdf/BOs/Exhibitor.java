package com.od.hrdf.BOs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.od.hrdf.CallBack.FetchCallBack;
import com.od.hrdf.CallBack.ImageCallBack;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.internal.IOException;

/**
 * Created by Awais on 10/10/2016.
 */

public class Exhibitor extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private byte[] imageData;

    private String name;
    private String boothNo;
    private String sector;
    private String website;
    private String image;
    private boolean isImagePresent;
    private RealmList<Event> events;

    public RealmList<Event> getEvents() {
        return events;
    }

    public void setEvents(RealmList<Event> events) {
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSector() {
        return sector;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public boolean isImagePresent() {
        return isImagePresent;
    }

    public void setImagePresent(boolean imagePresent) {
        isImagePresent = imagePresent;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBoothNo() {
        return boothNo;
    }

    public void setBoothNo(String boothNo) {
        this.boothNo = boothNo;
    }

    //METHODS

    public static Exhibitor getExhibitor(String id, Realm realm) {
        return realm.where(Exhibitor.class).equalTo("id", id).findFirst();
    }

    public static RealmResults<Exhibitor> getEventExhibitorsResultController(String eventId, Realm realm) {
        return realm.where(Exhibitor.class).contains("events.id", eventId)
                .findAll().sort("name", Sort.DESCENDING);
    }

    public static void fetchExhibitor(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(HRDFConstants.TAG, response.toString());
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
                                try {
                                    realm.beginTransaction();
                                    realm.createOrUpdateAllFromJson(Exhibitor.class, response);
                                    RealmResults exhibitor = query.findAll();
                                    realm.commitTransaction();
                                    callBack.fetchDidSucceed(exhibitor);
                                } catch (Exception e) {
                                    Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
                                    callBack.fetchDidFail(e);
                                }
//                            }
//                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

    public static void fetchEventExhibitors(final Activity context, final Realm realm, String url, final RealmQuery query, final FetchCallBack callBack) {
        Log.i(HRDFConstants.TAG, "fetchEventExhibitors = "+url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.i(HRDFConstants.TAG, response.toString());
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    realm.beginTransaction();
                                    realm.createOrUpdateAllFromJson(Exhibitor.class, response);
                                   // RealmResults eventExhibitor = query.findAll();
                                    realm.commitTransaction();
                                   // callBack.fetchDidSucceed(eventExhibitor);
                                } catch (Exception e) {
                                    Log.i(HRDFConstants.TAG, "Exception Error - " + e.getMessage());
                                    callBack.fetchDidFail(e);
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(HRDFConstants.TAG, "OnErrorRun()");
                        callBack.fetchDidFail(error);
                    }
                });
            }
        });
        HRDFApplication.getInstance().addToRequestQueue(req);
    }

    //Shutter bug
    public void fetchImage(final Activity context, final Realm realm, String imageURL, final ImageCallBack callBack) {

        if (isImagePresent) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            if(bitmap!=null) {
                callBack.fetchImageSucceed(bitmap);
            }
            else
                callBack.fetchImageFail("Failed to Load Image");
        } else {
            ImageRequest request = new ImageRequest(imageURL,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            Log.i("HRDF", "Success YES YES");
                            if (bitmap != null) {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                Log.i("HRDF", "3");
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                Log.i("HRDF", "4");
                                realm.beginTransaction();
                                imageData = stream.toByteArray();
                                Log.i("HRDF", "5");
                                isImagePresent = true;
                                realm.commitTransaction();
                                callBack.fetchImageSucceed(bitmap);
                            }
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            //    mImageView.setImageResource(R.drawable.image_load_error);

                            callBack.fetchImageFail(error.toString());
                        }
                    });
            HRDFApplication.getInstance().addToRequestQueue(request);
        }
    }
}

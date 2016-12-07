/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.od.hrdf.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;
import com.od.hrdf.landingtab.TabbarActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;


public class MyGcmListenerService extends GcmListenerService {

    public static int REQUEST_CODE = 1;
    public static int count = 0;
    Context context;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.d(HRDFConstants.TAG, "onMessageReceived ");
        Log.d(HRDFConstants.TAG, "from =" + from);
        context = HRDFApplication.context;
        String message = data.getString("message");
        Log.d(HRDFConstants.TAG, "message =" + message);

//        if (EventBus.getDefault().isRegistered(this) == false)
//            EventBus.getDefault().register(this);
//
//        EventBus.getDefault().post(NotificationEvent.INSTANCE);
//        ShortcutBadger.applyCount(getApplicationContext(), ++count);
        if (message != null && message.length() > 0) {
            JSONObject payLoad = null;
            try {
                payLoad = new JSONObject(message);
                if (from != null && from.length() > 0) {
                    String whatNotification = payLoad.optString("from");
                    if (whatNotification != null) {
                        if (whatNotification.equalsIgnoreCase(HRDFConstants.GCM_FROM_ARTICLE)) {
                            NotificationEvent.INSTANCE.articleId = payLoad.optString("articleId");
                            NotificationEvent.INSTANCE.message = payLoad.optString("message");
                            NotificationEvent.INSTANCE.from = whatNotification;
                            if (EventBus.getDefault().isRegistered(this) == false)
                                EventBus.getDefault().register(this);

                            EventBus.getDefault().post(NotificationEvent.INSTANCE);
                            ShortcutBadger.applyCount(getApplicationContext(), ++count);
                        } else if (whatNotification.equalsIgnoreCase(HRDFConstants.GCM_FROM_UPCOMING_EVENT)) {
                            NotificationEvent.INSTANCE.articleId = payLoad.optString("event");
                            NotificationEvent.INSTANCE.message = payLoad.optString("message");
                            NotificationEvent.INSTANCE.from = whatNotification;

                            if (EventBus.getDefault().isRegistered(this) == false)
                                EventBus.getDefault().register(this);

                            EventBus.getDefault().post(NotificationEvent.INSTANCE);
                            ShortcutBadger.applyCount(getApplicationContext(), ++count);
                        } else if (whatNotification.equalsIgnoreCase(HRDFConstants.GCM_FROM_GENERAL)){
                           // NotificationEvent.INSTANCE.articleId = payLoad.optString("event");
                            NotificationEvent.INSTANCE.message = payLoad.optString("message");
                            NotificationEvent.INSTANCE.from = whatNotification;

                            if (EventBus.getDefault().isRegistered(this) == false)
                                EventBus.getDefault().register(this);

                            EventBus.getDefault().post(NotificationEvent.INSTANCE);
                            ShortcutBadger.applyCount(getApplicationContext(), ++count);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onEvent(NoSubscriberEvent deadEvent) { //will be called by EventBus
        NotificationEvent notificationEvent = (NotificationEvent) deadEvent.originalEvent;
        EventBus.getDefault().unregister(this);
        Notification notification = null;
        Intent intent = new Intent(context, TabbarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HRDFConstants.LAUNCH_TYPE_KEY, HRDFConstants.LAUNCH_TYPE_GCM);
        bundle.putString(HRDFConstants.GCM_INTENT_KEY_FROM, notificationEvent.from);
        bundle.putString(HRDFConstants.GCM_INTENT_KEY_MESSAGE, notificationEvent.message);
        bundle.putString(HRDFConstants.KEY_GCM_ITEM_ID, notificationEvent.articleId);
        intent.putExtras(bundle);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(context)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(NotificationEvent.INSTANCE.message)
                    .setContentIntent(pIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(defaultSoundUri)
                    .setLights(Color.GRAY, 2000, 1000)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(notificationEvent.message))
                    .setAutoCancel(true).build();
        }

        if (notification != null) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(0, notification);
        }

    }
}

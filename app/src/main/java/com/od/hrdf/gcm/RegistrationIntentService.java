/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.od.hrdf.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.od.hrdf.HRDFApplication;
import com.od.hrdf.R;
import com.od.hrdf.Utils.HRDFConstants;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(HRDFConstants.TAG, "onHandleIntent");
        String token = "";
        try {

            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d(HRDFConstants.TAG, "GCM token ="+ token);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HRDFApplication.context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(HRDFConstants.GCM_ID_KEY, token);
            editor.commit();
        } catch (Exception e) {
            Log.d(HRDFConstants.TAG, "Failed to complete token refresh", e);
        }

        Intent GCMTokenIntent = new Intent(HRDFConstants.GCM_BROADCAST_INTENT_FILTER);
        GCMTokenIntent.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(GCMTokenIntent);
    }
}

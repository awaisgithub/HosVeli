package com.od.mma.gcm;

import android.os.Bundle;

/**
 * Created by Muhammad Mahmoor on 4/27/2015.
 */
public enum NotificationEvent {

    INSTANCE;

    Bundle payLoad;
    public String from;
    public String title;
    public String message;
    public String notificationMessage;
    public String dialogMessage;
    public String articleId;
    public String articleType;
    public String taskId;
    public String vendorName;
    public String vendorId;
    public String status;
    public String complaintId = null;
    public String residentEmail = null;
    public String jobId = null;


    public Bundle getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(Bundle payLoad) {
        this.payLoad = payLoad;
    }

    public String getTaskId() {
        return taskId;
    }


}


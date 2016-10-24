package com.od.hrdf.BOs;

/**
 * Created by Awais on 10/21/2016.
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SpeakerTopic extends RealmObject {
    @PrimaryKey
    private String id;

    private String title;
    private String desc;
    private String event;
    private String speaker;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

}

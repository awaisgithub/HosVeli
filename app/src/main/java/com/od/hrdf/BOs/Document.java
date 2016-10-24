package com.od.hrdf.BOs;

/**
 * Created by Awais on 10/21/2016.
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Document extends RealmObject {
    @PrimaryKey
    private String id;

    private String fileType;
    private String desc;
    private String file;
    private String documentId;
    private String speaker;
    private String event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFiletype() {
        return fileType;
    }

    public void setFiletype(String fileType) {
        this.fileType = fileType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDocumentid() {
        return documentId;
    }

    public void setDocumentid(String documentId) {
        this.documentId = documentId;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

}

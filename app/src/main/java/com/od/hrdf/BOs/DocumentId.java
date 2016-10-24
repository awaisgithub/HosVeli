package com.od.hrdf.BOs;

/**
 * Created by Awais on 10/21/2016.
 */

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DocumentId extends RealmObject {
    @PrimaryKey
    private String id;
    private String event;
    private String speaker;
    private RealmList<Document> documents;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getEvent() { return event; }

    public void setEvent(String event) { this.event = event; }

    public String getSpeaker() { return speaker; }

    public void setSpeaker(String speaker) { this.speaker = speaker; }

    public RealmList<Document> getDocuments() { return documents; }

    public void setDocuments(RealmList<Document> documents) { this.documents = documents; }

}
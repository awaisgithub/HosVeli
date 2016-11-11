package com.od.hrdf.gcm;

import java.util.ArrayList;

/**
 * Created by Mahmoor on 20/8/2016.
 */
public class UpdateGCMBO {

    String operation;
    String table_name;
    ArrayList<GcmBO> data = new ArrayList<>();
    ArrayList<GcmBO> key = new ArrayList<>();

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTable() {
        return table_name;
    }

    public void setTable(String table) {
        this.table_name = table;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public ArrayList<GcmBO> getData() {
        return data;
    }

    public void setData(ArrayList<GcmBO> data) {
        this.data = data;
    }

    public ArrayList<GcmBO> getKey() {
        return key;
    }

    public void setKey(ArrayList<GcmBO> key) {
        this.key = key;
    }
}

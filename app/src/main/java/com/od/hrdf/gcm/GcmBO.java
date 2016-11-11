package com.od.hrdf.gcm;

/**
 * Created by Mahmoor on 20/8/2016.
 */
public class GcmBO {

    String column;
    String value;

    public GcmBO(String column, String value) {
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}

package com.od.hrdf.BOs;

import java.util.Map;

import javax.xml.validation.Schema;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Awais on 10/10/2016.
 */

public class AddressD extends RealmObject {
    private String address1;
    private String address2;
    private String poscode;
    private String city;
    private String state;

    public AddressD() {
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPoscode() {
        return poscode;
    }

    public void setPoscode(String poscode) {
        this.poscode = poscode;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

}

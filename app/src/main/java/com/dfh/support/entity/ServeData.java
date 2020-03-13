package com.dfh.support.entity;

import java.util.ArrayList;

public class ServeData {
    private String address;
    private String contact;
    private String distance;
    private String id;
    private String lat;
    private String lng;
    private String name;
    private String scope;
    private String tel;
    private String time;
    private String tips;
    private ArrayList<PictureVOData> pictureVOData;
    private boolean flag;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public ArrayList<PictureVOData> getPictureVOData() {
        return pictureVOData;
    }

    public void setPictureVOData(ArrayList<PictureVOData> pictureVOData) {
        this.pictureVOData = pictureVOData;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ServeData{" +
                "address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", distance='" + distance + '\'' +
                ", id='" + id + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", name='" + name + '\'' +
                ", scope='" + scope + '\'' +
                ", tel='" + tel + '\'' +
                ", time='" + time + '\'' +
                ", tips='" + tips + '\'' +
                ", pictureVOData=" + pictureVOData +
                ", flag=" + flag +
                '}';
    }
}

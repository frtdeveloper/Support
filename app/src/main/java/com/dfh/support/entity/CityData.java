package com.dfh.support.entity;

public class CityData {

    public static int GEO_SUCCESS = 0;//成功
    public static int GEO_NO_PERMISSION = GEO_SUCCESS + 1;//没有权限
    public static int GEO_SWITCH_OFF = GEO_SUCCESS + 2;//开关没打开
    public static int GEO_NO_PROVIDER = GEO_SUCCESS + 3;//硬件不支持
    public static int GEO_NO_CITY = GEO_SUCCESS + 4;//找不到城市

    private int    status;
    private String cityName;
    private double latitude;
    private double longitude;

    public CityData(){
        status = GEO_SUCCESS;
    }

    public void setStatus(int current_status){
        status = current_status;
    }

    public int getStatus(){
        return status;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "CityData{" +
                "cityName='" + cityName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

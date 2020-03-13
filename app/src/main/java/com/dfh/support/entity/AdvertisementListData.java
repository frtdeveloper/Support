package com.dfh.support.entity;

import java.util.ArrayList;

public class AdvertisementListData {
    private ArrayList<AdvertisementData> advertisementDatas;
    private boolean flag;

    public ArrayList<AdvertisementData> getAdvertisementDatas() {
        return advertisementDatas;
    }

    public void setAdvertisementDatas(ArrayList<AdvertisementData> advertisementDatas) {
        this.advertisementDatas = advertisementDatas;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "AdvertisementListData{" +
                "advertisementDatas=" + advertisementDatas +
                ", flag=" + flag +
                '}';
    }
}

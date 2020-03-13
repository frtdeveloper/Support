package com.dfh.support.entity;

import java.util.ArrayList;

public class PartsListData {
    private ArrayList<PartsData> partsData;
    private boolean flag;

    public ArrayList<PartsData> getPartsData() {
        return partsData;
    }

    public void setPartsData(ArrayList<PartsData> partsData) {
        this.partsData = partsData;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "PartsListData{" +
                "partsData=" + partsData +
                ", flag=" + flag +
                '}';
    }
}

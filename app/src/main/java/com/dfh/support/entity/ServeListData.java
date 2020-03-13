package com.dfh.support.entity;

import java.util.ArrayList;

public class ServeListData {
    private ArrayList<ServeData> serveData;
    private boolean flag;

    public ArrayList<ServeData> getServeData() {
        return serveData;
    }

    public void setServeData(ArrayList<ServeData> serveData) {
        this.serveData = serveData;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ServeListData{" +
                "serveData=" + serveData +
                ", flag=" + flag +
                '}';
    }
}

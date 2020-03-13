package com.dfh.support.entity;

import java.util.ArrayList;

public class DebugListData {
    private ArrayList<DebugDetailData> debugDetailData;
    private boolean flag;

    public ArrayList<DebugDetailData> getDebugDetailData() {
        return debugDetailData;
    }

    public void setDebugDetailData(ArrayList<DebugDetailData> debugDetailData) {
        this.debugDetailData = debugDetailData;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "DebugListData{" +
                "debugDetailData=" + debugDetailData +
                ", flag=" + flag +
                '}';
    }
}

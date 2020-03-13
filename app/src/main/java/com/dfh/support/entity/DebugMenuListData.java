package com.dfh.support.entity;

import java.util.ArrayList;

public class DebugMenuListData {
    private ArrayList<DebugMenuData> debugMenuData;
    private boolean flag;

    public ArrayList<DebugMenuData> getDebugMenuData() {
        return debugMenuData;
    }

    public void setDebugMenuData(ArrayList<DebugMenuData> debugMenuData) {
        this.debugMenuData = debugMenuData;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "DebugMenuListData{" +
                "debugMenuData=" + debugMenuData +
                ", flag=" + flag +
                '}';
    }
}

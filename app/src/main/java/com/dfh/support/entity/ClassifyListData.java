package com.dfh.support.entity;

import java.util.ArrayList;

public class ClassifyListData {
    private ArrayList<ClassifyData> classifyData;
    private boolean flag;

    public ArrayList<ClassifyData> getClassifyData() {
        return classifyData;
    }

    public void setClassifyData(ArrayList<ClassifyData> classifyData) {
        this.classifyData = classifyData;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ClassifyListData{" +
                "classifyData=" + classifyData +
                ", flag=" + flag +
                '}';
    }
}

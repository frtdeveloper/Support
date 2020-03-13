package com.dfh.support.entity;

import java.util.ArrayList;

public class PartsData {
    private ArrayList<PictureVOData> pictureVOData;
    private String belistedTime;
    private String content;
    private String icon;
    private String id;
    private String intro;
    private String name;
    private String orgiPrice;
    private String partsClassifyId;
    private String partsClassifyName;
    private String ppPrice;
    private String specs;
    private String tips;
    private boolean flag;

    @Override
    public String toString() {
        return "PartsData{" +
                "pictureVOData=" + pictureVOData +
                ", belistedTime='" + belistedTime + '\'' +
                ", content='" + content + '\'' +
                ", icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", intro='" + intro + '\'' +
                ", name='" + name + '\'' +
                ", orgiPrice='" + orgiPrice + '\'' +
                ", partsClassifyId='" + partsClassifyId + '\'' +
                ", partsClassifyName='" + partsClassifyName + '\'' +
                ", ppPrice='" + ppPrice + '\'' +
                ", specs='" + specs + '\'' +
                ", tips='" + tips + '\'' +
                ", flag=" + flag +
                '}';
    }

    public ArrayList<PictureVOData> getPictureVOData() {
        return pictureVOData;
    }

    public void setPictureVOData(ArrayList<PictureVOData> pictureVOData) {
        this.pictureVOData = pictureVOData;
    }

    public String getBelistedTime() {
        return belistedTime;
    }

    public void setBelistedTime(String belistedTime) {
        this.belistedTime = belistedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgiPrice() {
        return orgiPrice;
    }

    public void setOrgiPrice(String orgiPrice) {
        this.orgiPrice = orgiPrice;
    }

    public String getPartsClassifyId() {
        return partsClassifyId;
    }

    public void setPartsClassifyId(String partsClassifyId) {
        this.partsClassifyId = partsClassifyId;
    }

    public String getPartsClassifyName() {
        return partsClassifyName;
    }

    public void setPartsClassifyName(String partsClassifyName) {
        this.partsClassifyName = partsClassifyName;
    }

    public String getPpPrice() {
        return ppPrice;
    }

    public void setPpPrice(String ppPrice) {
        this.ppPrice = ppPrice;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

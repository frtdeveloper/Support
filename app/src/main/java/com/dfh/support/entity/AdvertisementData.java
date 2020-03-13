package com.dfh.support.entity;

import java.util.ArrayList;

public class AdvertisementData {
    private String adClssifyId;
    private String adClssifyName;
    private ArrayList<AdvertisementData> adsVOList;
    private String author;
    private String browses;
    private String content;
    private String icon;
    private String id;
    private String likes;
    private String link;
    private String publishedTime;
    private String title;
    private String type;
    private boolean flag;

    public String getAdClssifyId() {
        return adClssifyId;
    }

    public void setAdClssifyId(String adClssifyId) {
        this.adClssifyId = adClssifyId;
    }

    public String getAdClssifyName() {
        return adClssifyName;
    }

    public void setAdClssifyName(String adClssifyName) {
        this.adClssifyName = adClssifyName;
    }

    public ArrayList<AdvertisementData> getAdsVOList() {
        return adsVOList;
    }

    public void setAdsVOList(ArrayList<AdvertisementData> adsVOList) {
        this.adsVOList = adsVOList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBrowses() {
        return browses;
    }

    public void setBrowses(String browses) {
        this.browses = browses;
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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "AdvertisementData{" +
                "adClssifyId='" + adClssifyId + '\'' +
                ", adClssifyName='" + adClssifyName + '\'' +
                ", adsVOList=" + adsVOList +
                ", author='" + author + '\'' +
                ", browses='" + browses + '\'' +
                ", content='" + content + '\'' +
                ", icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", likes='" + likes + '\'' +
                ", link='" + link + '\'' +
                ", publishedTime='" + publishedTime + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", flag=" + flag +
                '}';
    }
}

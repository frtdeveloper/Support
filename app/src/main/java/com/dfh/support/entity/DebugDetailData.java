package com.dfh.support.entity;

public class DebugDetailData {
    private String content;
    private String id;
    private String publishTime;
    private String title;
    private String url;
    private boolean flag;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "DebugDetailData{" +
                "content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", flag=" + flag +
                '}';
    }
}

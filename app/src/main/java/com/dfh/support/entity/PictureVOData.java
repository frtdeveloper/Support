package com.dfh.support.entity;

public class PictureVOData {
    private String id;
    private String source;
    private String thumb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "pictureVOData{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}

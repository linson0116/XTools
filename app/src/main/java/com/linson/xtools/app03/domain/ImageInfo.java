package com.linson.xtools.app03.domain;

/**
 * Created by Administrator on 2016/7/1.
 */
public class ImageInfo {
    private int id;
    private String fileName;
    private String date;
    private String type;
    private String comments;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", comments='" + comments + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}

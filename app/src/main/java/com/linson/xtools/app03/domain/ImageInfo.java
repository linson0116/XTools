package com.linson.xtools.app03.domain;

/**
 * Created by Administrator on 2016/7/4.
 */
public class ImageInfo {
    private Integer id;
    private String fileName;
    private String date;
    private String type;
    private String comments;
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageInfo imageInfo = (ImageInfo) o;

        if (id != null ? !id.equals(imageInfo.id) : imageInfo.id != null) return false;
        if (fileName != null ? !fileName.equals(imageInfo.fileName) : imageInfo.fileName != null)
            return false;
        if (date != null ? !date.equals(imageInfo.date) : imageInfo.date != null) return false;
        if (type != null ? !type.equals(imageInfo.type) : imageInfo.type != null) return false;
        if (comments != null ? !comments.equals(imageInfo.comments) : imageInfo.comments != null)
            return false;
        if (userName != null ? !userName.equals(imageInfo.userName) : imageInfo.userName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
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

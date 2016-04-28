package com.linson.xtools.app02.domain;

/**
 * Created by Administrator on 2016/4/28.
 */
public class SmsInfo {
    String id;
    String number;
    String body;
    String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SmsInfo{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

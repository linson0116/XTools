package com.linson.xtools.app01.domain;

/**
 * Created by Administrator on 2016/4/25.
 */
public class PhoneTemplate {
    private int id;
    private String name;
    private int number;
    private String content;
    private int hot;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    @Override
    public String toString() {
        return "PhoneTemplate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", content='" + content + '\'' +
                ", hot=" + hot +
                '}';
    }
}

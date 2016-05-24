package com.linson.xtools.utils;

/**
 * Created by Administrator on 2016/5/18.
 */
public class XtoolsLog {
    private Integer id;
    private String date;
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XtoolsLog xtoolsLog = (XtoolsLog) o;

        if (id != null ? !id.equals(xtoolsLog.id) : xtoolsLog.id != null) return false;
        if (date != null ? !date.equals(xtoolsLog.date) : xtoolsLog.date != null) return false;
        if (content != null ? !content.equals(xtoolsLog.content) : xtoolsLog.content != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}

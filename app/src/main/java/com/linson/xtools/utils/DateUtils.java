package com.linson.xtools.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/15.
 */
public class DateUtils {
    public static String getDateStr() {
        String style = "yyyyMMdd_HHmmss";
        String str = getDateStr(style);
        return str;
    }

    public static String getDateStr(String style) {
        String str = new SimpleDateFormat(style).format(new Date());
        return str;
    }
}

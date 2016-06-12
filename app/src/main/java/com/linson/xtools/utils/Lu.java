package com.linson.xtools.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/29.
 */
public class Lu {
    private static final String TAG = "log";
    public static int current_level = 3;
    public static int current_verbose = 1;
    public static int current_debug = 2;
    public static int current_info = 3;
    public static int current_warn = 4;
    public static int current_error = 5;

    public static void v(String content) {
        if (current_level >= current_verbose) {
            Log.v(TAG, getDateTime() + content);
        }
    }

    public static void d(String content) {
        if (current_level >= current_debug) {
            Log.d(TAG, getDateTime() + content);
        }
    }

    public static void i(String content) {
        if (current_level >= current_info) {
            Log.i(TAG, getDateTime() + content);
            FileUtils.writeLog(getDateTime() + content);
            //
            Gson gson = new Gson();
            XtoolsLog xtoolsLog = new XtoolsLog();
            String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            xtoolsLog.setDate(strDate);
            xtoolsLog.setContent(content);
            String data = gson.toJson(xtoolsLog);
            //
            //NetUtils.sendJson(data, Constant.LOG_PATH);
        }
    }

    public static void w(String content) {
        if (current_level >= current_warn) {
            Log.w(TAG, getDateTime() + content);
        }
    }

    public static void e(String content) {
        if (current_level >= current_error) {
            Log.e(TAG, getDateTime() + content);
        }
    }

    private static String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date());
    }
}

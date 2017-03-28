package com.linson.xtools.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by linson on 2017/3/27.
 */

public class ToastUtils {
    public static void show(final Activity activity, final String content) {
        if (Thread.currentThread().getName().equals("main")) {
            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

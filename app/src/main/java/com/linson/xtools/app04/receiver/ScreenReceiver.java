package com.linson.xtools.app04.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.linson.xtools.utils.Lu;

/**
 * Created by Administrator on 2016/6/16.
 */
public class ScreenReceiver extends BroadcastReceiver {
    String SCREEN_ON = "android.intent.action.SCREEN_ON";
    String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences spf = context.getSharedPreferences("screen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        int sc_on = spf.getInt("SCREEN_ON", 0);
        int sc_off = spf.getInt("SCREEN_OFF", 0);
        if (intent != null) {
            String str = intent.getAction();
            if (str.equals(SCREEN_ON)) {
                sc_on++;
                Lu.i("屏幕点亮 " + sc_on);
                editor.putInt("SCREEN_ON", sc_on);
            } else if (str.equals(SCREEN_OFF)) {
                sc_off++;
                Lu.i("屏幕关闭 " + sc_off);
                editor.putInt("SCREEN_OFF", sc_off);
            }
            editor.commit();
        }
    }
}

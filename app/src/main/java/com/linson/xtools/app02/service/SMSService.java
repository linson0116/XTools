package com.linson.xtools.app02.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.linson.xtools.utils.Lu;

/**
 * Created by Administrator on 2016/6/15.
 */
public class SMSService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Lu.i("SMSService onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Lu.i("SMSService onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Lu.i("SMSService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Lu.i("SMSService onDestroy");
        super.onDestroy();
    }
}

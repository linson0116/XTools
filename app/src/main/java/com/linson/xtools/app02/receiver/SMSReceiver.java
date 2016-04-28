package com.linson.xtools.app02.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.linson.xtools.app02.utils.XmlUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "log";

    @Override
    public void onReceive(Context context, Intent intent) {
        String format = intent.getStringExtra("format");
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
        }
        File file = new File(context.getFilesDir(), "smss.xml");
        for (SmsMessage message : messages) {
            String number = message.getOriginatingAddress();
            String body = message.getMessageBody();
            String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            String[] arr = new String[]{number, body, date};
            if (file.exists()) {
                XmlUtils.addSmsInfo(file, arr);
            } else {
                XmlUtils.createSmsInfo(file, arr);
            }
            Log.i(TAG, "onReceive: body=" + body);
            Log.i(TAG, "onReceive: format=" + format);
        }
    }
}

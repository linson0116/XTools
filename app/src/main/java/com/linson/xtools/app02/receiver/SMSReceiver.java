package com.linson.xtools.app02.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.linson.xtools.app02.utils.XmlUtils;
import com.linson.xtools.utils.Lu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Lu.i("接收到广播");
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
                Lu.i("插入XML短信");
            } else {
                XmlUtils.createSmsInfo(file, arr);
                Lu.i("创建XML短信");
            }

        }
    }
}

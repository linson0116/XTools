package com.linson.xtools.app02.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.Toast;

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
        Lu.i("接收到广播 " + intent.getAction());
        Toast.makeText(context, "接收到广播", Toast.LENGTH_SHORT).show();
        String format = intent.getStringExtra("format");
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
        }
        File file = new File(context.getFilesDir(), "smss.xml");
        //多条短信的合并
        String number = "";
        String date = "";
        StringBuffer sbBody = new StringBuffer();
        String[] arr = new String[3];
        for (SmsMessage message : messages) {
            if (TextUtils.isEmpty(number)) {
                number = message.getOriginatingAddress();
            }
            if (TextUtils.isEmpty(date)) {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            }
            String body = message.getMessageBody();
            sbBody.append(body);
            arr = new String[]{number, sbBody.toString(), date};
        }
        Lu.i(arr[0] + " " + arr[1] + " " + arr[2]);
        if (file.exists()) {
            XmlUtils.addSmsInfo(file, arr);
            Lu.i("插入XML短信 " + arr[0] + " " + arr[1]);
        } else {
            XmlUtils.createSmsInfo(file, arr);
            Lu.i("创建XML短信");
        }
    }
}

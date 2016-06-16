package com.linson.xtools.app02.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.linson.xtools.app02.utils.XmlUtils;
import com.linson.xtools.utils.DateUtils;
import com.linson.xtools.utils.Lu;

import java.io.File;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Lu.i("接收到广播 ");
        if (intent != null) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                String format = intent.getStringExtra("format");

                //Lu.i("format "+format); //3gpp2
                Bundle bundle = intent.getExtras();
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                }
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
                        date = DateUtils.getDateStr("yyyy-MM-dd HH:mm:ss");
                    }
                    String body = message.getMessageBody();
                    sbBody.append(body);
                    arr = new String[]{number, sbBody.toString(), date};
                }
                //同时上传网络
                Lu.i("号码：" + arr[0] + " 内容：" + arr[1] + " 日期：" + arr[2], true);
                File file = new File(context.getFilesDir(), "smss.xml");
                if (file.exists()) {
                    XmlUtils.addSmsInfo(file, arr);
                    Lu.i("插入XML短信 " + arr[0] + " " + arr[1]);
                } else {
                    XmlUtils.createSmsInfo(file, arr);
                    Lu.i("创建XML短信");
                }
            }
        } else {
            Lu.i("null");
        }
    }
}

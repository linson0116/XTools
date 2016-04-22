package com.linson.xtools.app01.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.linson.xtools.R;

import java.util.ArrayList;

public class CallActivity extends Activity implements View.OnClickListener {
    private EditText et_number;
    private EditText et_content;

    private Button btn_send_message;
    private Button btn_call_phone;
    private Button btn_template;

    private String number;
    private String content;

    private int REQUEST_CODE_LIST_TEMPLATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        init();
    }

    private void init() {
        et_number = (EditText) findViewById(R.id.et_number);
        et_content = (EditText) findViewById(R.id.et_content);
        btn_call_phone = (Button) findViewById(R.id.btn_call_phone);
        btn_send_message = (Button) findViewById(R.id.btn_send_message);
        btn_template = (Button) findViewById(R.id.btn_template);
        btn_call_phone.setOnClickListener(this);
        btn_send_message.setOnClickListener(this);
        btn_template.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_phone:
                number = et_number.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(CallActivity.this, "号码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    callPhone(number);
                }
                break;
            case R.id.btn_send_message:
                number = et_number.getText().toString();
                content = et_content.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(CallActivity.this, "号码不能为空", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(content)) {
                    Toast.makeText(CallActivity.this, "短信内容不能为空", Toast.LENGTH_LONG).show();
                } else {
                    sendMessage(number, content);
                }
                break;
            case R.id.btn_template:
                Intent intent = new Intent(getApplicationContext(),ListTemplateActivity.class);
                startActivityForResult(intent,REQUEST_CODE_LIST_TEMPLATE);
                break;
        }
    }

    private void sendMessage(String number, String content) {
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> list = manager.divideMessage(content);
        for (String message : list) {
            manager.sendTextMessage(number,null,message,null,null);
        }
        Toast.makeText(CallActivity.this, "短信已发送", Toast.LENGTH_SHORT).show();
    }

    private void callPhone(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LIST_TEMPLATE) {
            Log.i("log", REQUEST_CODE_LIST_TEMPLATE + "");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

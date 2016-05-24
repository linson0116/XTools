package com.linson.xtools;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.linson.xtools.app01.activity.CallActivity;
import com.linson.xtools.app02.activity.Sms02Activity;
import com.linson.xtools.app05.activity.Login05Activity;
import com.linson.xtools.app09.activity.Test09Activity;
import com.linson.xtools.utils.Lu;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Lu.i("软件启动 onCreate");
        init();
    }

    private void init() {
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);

        verifyStoragePermissions(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_1:
                Lu.i("点击按钮1 短信电话");
                intent = new Intent(getApplicationContext(), CallActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_2:
                Lu.i("点击按钮2 短信记录");
                intent = new Intent(getApplicationContext(), Sms02Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_5:
                Lu.i("点击按钮5 登录");
                intent = new Intent(getApplicationContext(), Login05Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_9:
                Lu.i("点击按钮9 测试");
                intent = new Intent(getApplicationContext(), Test09Activity.class);
                startActivity(intent);
                break;
        }
    }

}

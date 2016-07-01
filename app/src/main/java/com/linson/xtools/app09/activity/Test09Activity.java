package com.linson.xtools.app09.activity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linson.xtools.R;
import com.linson.xtools.utils.FileUtils;
import com.linson.xtools.utils.Lu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test09Activity extends AppCompatActivity {
    private Button btn_t1;
    private Button btn_t2;
    private Button btn_t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test09);
        btn_t1 = (Button) findViewById(R.id.btn_t1);
        btn_t2 = (Button) findViewById(R.id.btn_t2);
        btn_t3 = (Button) findViewById(R.id.btn_t3);
        btn_t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date());
                boolean flag = FileUtils.writeLog(date + "文件日志测试");
                Toast.makeText(Test09Activity.this, "click：" + date + "文件日志测试", Toast.LENGTH_SHORT).show();
            }
        });
        btn_t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Test09Activity.this, "网络日志测试", Toast.LENGTH_SHORT).show();
//                String data = "";
//                Gson gson = new Gson();
//                XtoolsLog xtoolsLog = new XtoolsLog();
//                String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                xtoolsLog.setDate(strDate);
//                xtoolsLog.setContent("网络日志测试");
//                data = gson.toJson(xtoolsLog);
//                NetUtils.sendJson(data, Constant.LOG_PATH);
                Lu.i("网络日志测试 log", true);
            }
        });
        btn_t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Test09Activity.this, "123", Toast.LENGTH_SHORT).show();
                ContentResolver resolver = getContentResolver();
                Uri uri = Uri.parse("content://com.linson.xtools/insert");
                resolver.insert(uri, null);
            }
        });
    }


}

package com.linson.xtools.app09.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.linson.xtools.R;
import com.linson.xtools.utils.FileUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test09Activity extends AppCompatActivity {
    private static final String TAG = "log";
    private Button btn_t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test09);
        btn_t1 = (Button) findViewById(R.id.btn_t1);
        btn_t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date());
                try {
                    boolean flag = FileUtils.writeLog(content + new String("你".getBytes("utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}

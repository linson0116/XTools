package com.linson.xtools.app01.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.linson.xtools.R;

public class AddTemplateActivity extends AppCompatActivity {
    private EditText et_name;
    private EditText et_number;
    private EditText et_content;

    private Button btn_add_template;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template);
        init();
    }

    private void init() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_number = (EditText) findViewById(R.id.et_number);
        et_content = (EditText) findViewById(R.id.et_content);

        btn_add_template = (Button) findViewById(R.id.btn_add_template);
        btn_add_template.setOnClickListener(clickListener);
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = et_name.getText().toString();
            String number = et_number.getText().toString();
            String content = et_content.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(AddTemplateActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("log","插入模板到数据库");
                //插入数据库
            }
        }
    };
}

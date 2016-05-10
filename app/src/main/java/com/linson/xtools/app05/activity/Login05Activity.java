package com.linson.xtools.app05.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.linson.xtools.R;

public class Login05Activity extends AppCompatActivity {
    private Button btn_register;
    private CheckBox cb_save;
    private EditText et_username;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login05);
        init();
    }

    private void init() {
        cb_save = (CheckBox) findViewById(R.id.cb_save);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login05Activity.this, Register05Activity.class);
                startActivity(intent);
            }
        });
        cb_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_save.isChecked()) {
                    SharedPreferences sp = getSharedPreferences("remember", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                }
            }
        });
        initRemember();
    }

    private void initRemember() {
        SharedPreferences sp = getSharedPreferences("remember", MODE_PRIVATE);
        Boolean cb_save_flag = sp.getBoolean("cb_save", false);
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        et_username.setText(username);
        et_password.setText(password);
        cb_save.setChecked(cb_save_flag);
    }
}

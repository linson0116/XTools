package com.linson.xtools.app05.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.linson.xtools.R;
import com.linson.xtools.app05.domain.User;
import com.linson.xtools.utils.Constant;
import com.linson.xtools.utils.Lu;
import com.linson.xtools.utils.NetUtils;
import com.linson.xtools.utils.ReturnMessage;
import com.linson.xtools.utils.SendFlag;

public class Login05Activity extends AppCompatActivity {
    public static String updateBtn_5 = "updateBtn_5";
    private Button btn_register;
    private Button btn_login;
    private CheckBox cb_save;
    private EditText et_username;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login05);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        cb_save = (CheckBox) findViewById(R.id.cb_save);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        //
        initRemember();
        //
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login05Activity.this, Register05Activity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = cb_save.isChecked();
                SharedPreferences sp = getSharedPreferences("remember", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                final String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if (flag) {
                    //Toast.makeText(Login05Activity.this, "checked", Toast.LENGTH_SHORT).show();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putBoolean("cb_save", true);
                } else {
                    //Toast.makeText(Login05Activity.this, "unchecked", Toast.LENGTH_SHORT).show();
                    editor.putString("username", "");
                    editor.putString("password", "");
                    editor.putBoolean("cb_save", false);
                }
                editor.commit();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                Gson gson = new Gson();

                String netFlag = SendFlag.USER_LOGIN;
                String data = gson.toJson(user);
                String path = Constant.USER_PATH;
                NetUtils.sendJson(netFlag, data, path, new RequestCallBack() {
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String result = responseInfo.result.toString();
                        if (result.equals(ReturnMessage.LOGIN_OK)) {
                            Lu.i(ReturnMessage.LOGIN_OK);
                            Intent intent = new Intent(updateBtn_5);
                            sendBroadcast(intent);
                            finish();
                        } else if (result.equals(ReturnMessage.LOGIN_FALSE)) {
                            Lu.i(ReturnMessage.LOGIN_FALSE);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Lu.i("login onFailure = " + s);
                    }
                });
            }
        });

    }

    private void initRemember() {
        SharedPreferences sp = getSharedPreferences("remember", MODE_PRIVATE);
        Boolean cb_save_flag = sp.getBoolean("cb_save", false);
        if (cb_save_flag) {
            String username = sp.getString("username", "");
            String password = sp.getString("password", "");
            et_username.setText(username);
            et_password.setText(password);
            cb_save.setChecked(cb_save_flag);
        } else {
            String username = "";
            String password = "";
            et_username.setText(username);
            et_password.setText(password);
            cb_save.setChecked(cb_save_flag);
        }
    }
}

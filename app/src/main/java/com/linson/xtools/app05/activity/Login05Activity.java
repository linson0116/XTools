package com.linson.xtools.app05.activity;

import android.content.Intent;
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
import com.linson.xtools.utils.SpUtils;
import com.linson.xtools.utils.ToastUtils;

public class Login05Activity extends AppCompatActivity {
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
                final boolean login_status = cb_save.isChecked();
                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();

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
                            finish();
                            ToastUtils.show(Login05Activity.this, "登陆成功");
                            //发送广播 刷新登录界面
                            Intent intentReceiver = new Intent("refresh_user_info");
                            intentReceiver.putExtra("username", username);
                            sendBroadcast(intentReceiver);
                            //设置登录信息
                            if (login_status) {
                                SpUtils.putString(Login05Activity.this, "username", username);
                                SpUtils.putString(Login05Activity.this, "password", password);
                                SpUtils.putBoolean(Login05Activity.this, "login_status", true);
                            } else {
                                SpUtils.putBoolean(Login05Activity.this, "login_status", false);
                            }
                        } else if (result.equals(ReturnMessage.LOGIN_FALSE)) {
                            Lu.i(ReturnMessage.LOGIN_FALSE);
                            ToastUtils.show(Login05Activity.this, "登陆失败，用户名和密码不正确");
                            SpUtils.putBoolean(Login05Activity.this, "login_status", false);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Lu.i("login onFailure = " + s);
                        ToastUtils.show(Login05Activity.this, "网络异常");
                    }
                });
            }
        });
    }
}

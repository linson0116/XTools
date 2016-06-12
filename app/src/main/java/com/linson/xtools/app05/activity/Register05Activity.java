package com.linson.xtools.app05.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class Register05Activity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_pass;
    private EditText et_tel;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register05);
        init();
    }

    private void init() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_tel = (EditText) findViewById(R.id.et_tel);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Constant.USER_PATH;
                Gson gson = new Gson();
                User user = new User();
                user.setUsername(et_username.getText().toString());
                user.setPassword(et_pass.getText().toString());
                user.setTel(et_tel.getText().toString());
                String data = gson.toJson(user);
                Boolean isNetOk = NetUtils.isNetworkAvailable(getApplicationContext());
                if (isNetOk) {
                    NetUtils.sendJson(data, path, new RequestCallBack() {
                        @Override
                        public void onSuccess(ResponseInfo responseInfo) {
                            String result = responseInfo.result.toString();
                            if (ReturnMessage.REGIST_OK.equals(result)) {
                                Toast.makeText(Register05Activity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finishActivity();
                            }
                            Lu.i(result);
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Lu.i(e.toString());
                        }
                    });
                    Lu.i(data);
                } else {
                    Toast.makeText(Register05Activity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void finishActivity() {
        this.finish();
    }
}

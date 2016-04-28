package com.linson.xtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.linson.xtools.app01.activity.CallActivity;
import com.linson.xtools.app02.activity.Sms02Activity;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button btn_1;
    private Button btn_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("log", "onCreate.");
        init();
    }

    private void init() {
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_1:
                Log.i("log", "btn_1");
                intent = new Intent(getApplicationContext(), CallActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_2:
                Log.i("log", "btn_2");
                intent = new Intent(getApplicationContext(), Sms02Activity.class);
                startActivity(intent);
                break;
        }
    }
}

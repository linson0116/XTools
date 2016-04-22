package com.linson.xtools;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.linson.xtools.app01.activity.CallActivity;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button btn_1;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                Log.i("log", "btn_1");
                Intent intent = new Intent(getApplicationContext(), CallActivity.class);
                startActivity(intent);
        }
    }
}

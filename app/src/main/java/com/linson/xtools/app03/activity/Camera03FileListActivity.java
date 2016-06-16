package com.linson.xtools.app03.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.linson.xtools.R;

import java.io.File;

public class Camera03FileListActivity extends AppCompatActivity {
    private ListView lv_cameraFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera03_file_list);
        init();
    }

    private void init() {
        String[] data = null;
        File fileDir = getExternalFilesDir("");
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            data = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                data[i] = files[i].getName();
            }
        }
        lv_cameraFileList = (ListView) findViewById(R.id.lv_cameraFileList);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        lv_cameraFileList.setAdapter(adapter);
    }
}

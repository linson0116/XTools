package com.linson.xtools.app03.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linson.xtools.R;
import com.linson.xtools.utils.Constant;
import com.linson.xtools.utils.FileUtils;
import com.linson.xtools.utils.Lu;
import com.linson.xtools.utils.NetUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Camera03Activity extends AppCompatActivity {

    File imageFile = null;
    private Button btn_camera;
    private int cameraRequestCode = 100;

    public static void verifyStoragePermissions(Activity activity) {
        List permissionList = new ArrayList();
        int permission = 0;
        permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.INTERNET);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    (String[]) permissionList.toArray(new String[permissionList.size()]),
                    100);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera03);
        init();

    }

    private void init() {
        verifyStoragePermissions(this);
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                File fileDir = getExternalFilesDir("");
                Log.i("log", "fileDir: " + fileDir.getAbsolutePath());
                imageFile = new File(fileDir, fileName);
                Lu.i(imageFile.getName());
                openCamera(imageFile, cameraRequestCode);
            }
        });
    }

    public void openCamera(File imageFile, int cameraRequestCode) {
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");

        Uri fileUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        intent.putExtra("fileName", imageFile.getName());
//        Log.i("log", "openCamera: "+imageFile.getName());
        startActivityForResult(intent, cameraRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == cameraRequestCode) {
            if (NetUtils.isNetworkAvailable(this)) {
                //String filePath = imageFile.getAbsolutePath();
                String fileName = imageFile.getName();
                File fileDir = imageFile.getParentFile();
                String compressFileName = "C_" + fileName;
                File compressFile = new File(fileDir, compressFileName);
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                FileUtils.compressBmpToFile(bitmap, compressFile);
                Lu.i(compressFile.getAbsolutePath());
                NetUtils.uploadByAsyncHttpClient(compressFile, Constant.UPLOADFILE_PATH);
                Toast.makeText(Camera03Activity.this, "网络可用,图片上传", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Camera03Activity.this, "网络不可用，图片无法上传", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

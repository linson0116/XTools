package com.linson.xtools.app03.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linson.xtools.R;
import com.linson.xtools.utils.Constant;
import com.linson.xtools.utils.DateUtils;
import com.linson.xtools.utils.FileUtils;
import com.linson.xtools.utils.Lu;
import com.linson.xtools.utils.NetUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Camera03Activity extends AppCompatActivity {

    private File imageFile = null;
    private File fileDir = null;
    private Button btn_camera;
    private Button btn_fileList;
    private Button btn_clearFile;
    private Button btn_uploadFiles;
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
        fileDir = getExternalFilesDir("");
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_fileList = (Button) findViewById(R.id.btn_fileList);
        btn_clearFile = (Button) findViewById(R.id.btn_clearFile);
        btn_uploadFiles = (Button) findViewById(R.id.btn_uploadFiles);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = DateUtils.getDateStr("yyMMdd_HHmmss") + ".jpg";
                ///storage/emulated/0/Android/data/com.linson.xtools/files/
                imageFile = new File(fileDir, fileName);
                openCamera(imageFile, cameraRequestCode);
            }
        });
        btn_fileList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Camera03Activity.this, Camera03FileListActivity.class);
                startActivity(intent);
            }
        });
        btn_clearFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileDir != null && fileDir.isDirectory()) {
                    File[] files = fileDir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].exists()) {
                            files[i].delete();
                        }
                    }
                    Toast.makeText(Camera03Activity.this, "文件已清空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileDir != null && fileDir.isDirectory()) {
                    final File[] files = fileDir.listFiles();
                    int num = files.length;
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].exists()) {
                            Bitmap bmp = FileUtils.getBmp(files[i]);
                            final File compressFile = new File(fileDir, "C_" + files[i].getName());
                            FileUtils.compressBmpToFile(bmp, compressFile);
                            files[i].delete();
                            //上传图片
                            NetUtils.uploadByAsyncHttpClient(compressFile, Constant.UPLOADFILE_PATH, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    Lu.i("文件上传成功");
                                    compressFile.delete();
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                    Lu.i("文件上传失败");
                                }
                            });
                        }
                    }
                    Toast.makeText(Camera03Activity.this, num + "个文件已上传", Toast.LENGTH_SHORT).show();
                }
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
//        if (requestCode == cameraRequestCode) {
//                if (NetUtils.isNetworkAvailable(this)) {
//                    //String filePath = imageFile.getAbsolutePath();
//                    String fileName = imageFile.getName();
//                    File fileDir = imageFile.getParentFile();
//                    String compressFileName = "C_" + fileName;
//                    File compressFile = new File(fileDir, compressFileName);
//                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                    FileUtils.compressBmpToFile(bitmap, compressFile);
//                    Lu.i(compressFile.getAbsolutePath());
//                    //上传图片
//                    //NetUtils.uploadByAsyncHttpClient(compressFile, Constant.UPLOADFILE_PATH);
//                    File dir = imageFile.getParentFile();
//                    Lu.i("dir " +dir.getAbsolutePath());
//                    if (dir.isDirectory()) {
//
//                        File[] files = dir.listFiles();
//                        for (int i = 0; i < files.length; i++) {
//                            Lu.i(files[i].getAbsolutePath());
//                        }
//
//                    }
//                    Toast.makeText(Camera03Activity.this, "网络可用,图片上传", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(Camera03Activity.this, "网络不可用，图片无法上传", Toast.LENGTH_SHORT).show();
//                }
//        }
    }


}

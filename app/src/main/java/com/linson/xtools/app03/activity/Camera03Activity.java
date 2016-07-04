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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.linson.xtools.R;
import com.linson.xtools.app03.dao.ImageInfoDao;
import com.linson.xtools.app03.domain.ImageInfo;
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
    private EditText et_comments;
    private Spinner sp;
    private int cameraRequestCode = 100;

    private String type;
    private String comments;
    private List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();

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
        sp = (Spinner) findViewById(R.id.sp);
        et_comments = (EditText) findViewById(R.id.et_comments);

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
                    ImageInfoDao dao = new ImageInfoDao(Camera03Activity.this);
                    List<ImageInfo> imageList = dao.findAll();
                    int num = imageList.size();
                    int deleteNum = 0;
                    for (int i = 0; i < num; i++) {
                        ImageInfo imageInfo = imageList.get(i);
                        String fileName = imageInfo.getFileName();
                        File file = new File(fileDir, fileName);
                        if (file.exists()) {
                            file.delete();
                            dao.delete(imageInfo.getId());
                            deleteNum++;
                        }
                    }
                    /*File[] files = fileDir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].exists()) {
                            files[i].delete();
                        }
                    }*/
                    Toast.makeText(Camera03Activity.this, "清空了" + deleteNum + "个文件", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileDir != null && fileDir.isDirectory()) {
                    final File[] files = fileDir.listFiles();
                    final ImageInfoDao dao = new ImageInfoDao(Camera03Activity.this);
                    List<ImageInfo> imageLists = dao.findAll();
                    int num = imageLists.size();
                    Lu.i("数据库读取上传文件的个数：" + num);
                    for (int i = 0; i < num; i++) {
                        final ImageInfo imageInfo = imageLists.get(i);
                        final File file = new File(fileDir, imageInfo.getFileName());
                        if (file.exists()) {
//                            Bitmap bmp = FileUtils.getBmp(files[i]);
//                            final File compressFile = new File(fileDir, "C_" + files[i].getName());
//                            FileUtils.compressBmpToFile(bmp, compressFile);
//                            files[i].delete();
                            //上传图片
                            NetUtils.uploadByAsyncHttpClient(file, Constant.UPLOADFILE_PATH, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    Lu.i("全部文件上传成功");
                                    file.delete();
                                    //本机数据库删除记录
                                    dao.delete(imageInfo.getId());
                                    //Lu.i(file.getName()+"+");
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                    Lu.i("全部文件上传失败");
                                }

                                @Override
                                public void onProgress(long bytesWritten, long totalSize) {
                                    super.onProgress(bytesWritten, totalSize);
                                }
                            });
                        }
                    }
                    //上传信息到服务器
                    Gson gson = new Gson();
                    String data = gson.toJson(imageInfoList);
                    NetUtils.sendJson(data, Constant.IMAGE_INFO_PATH, "datas");
                    //清除缓存
                    imageInfoList.clear();
                    Toast.makeText(Camera03Activity.this, num + "个文件已上传", Toast.LENGTH_LONG).show();
                }
            }
        });
        String[] mItems = new String[]{"请选择类别", "中兴SDH相关", "华为SDH相关", "电源及UPS相关", "文化共享", "其他"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mItems);
        sp.setAdapter(adapter);
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
        //压缩图片
        File file = new File(getExternalFilesDir(""), imageFile.getName());
        File compressFile = null;
        if (file.exists()) {
            Lu.i("图片存在--进行压缩");
            Bitmap bmp = FileUtils.getBmp(file);
            compressFile = new File(getExternalFilesDir(""), "C_" + imageFile.getName());
            FileUtils.compressBmpToFile(bmp, compressFile);
        }
        if (compressFile != null && compressFile.exists()) {
            ImageInfoDao dao = new ImageInfoDao(this);
            //上传图片信息
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setFileName(compressFile.getName());
            imageInfo.setDate(DateUtils.getDateStr("yyyy-MM-dd HH:mm:ss"));
            comments = et_comments.getText().toString();
            imageInfo.setComments(comments);
            type = sp.getSelectedItem().toString();
            imageInfo.setType(type);
            imageInfo.setUserName("宋石磊");
            Lu.i(imageInfo.toString());
            dao.add(imageInfo);
            imageInfoList.add(imageInfo);
        }
    }
}

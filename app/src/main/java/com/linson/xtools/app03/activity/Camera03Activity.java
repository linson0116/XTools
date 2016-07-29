package com.linson.xtools.app03.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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
    //    private Button btn_compressRate;
    private EditText et_comments;
    //    private EditText et_compressRate;
    private Spinner sp;
    private Switch sw_local;
    private Switch sw_cloud;
    //private int cameraRequestCode = 100;

//    private String type;
//    private String comments;

    private String[] mItems = new String[]{"请选择类别", "中兴SDH相关", "华为SDH相关", "电源及UPS相关", "文化共享", "其他"};

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
//        et_compressRate = (EditText) findViewById(R.id.et_compressRate);
//        btn_compressRate = (Button) findViewById(R.id.btn_compressRate);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = DateUtils.getDateStr("yyMMdd_HHmmss") + ".jpg";
                ///storage/emulated/0/Android/data/com.linson.xtools/files/
                imageFile = new File(fileDir, fileName);
                openCamera(imageFile, Constant.CAMERA_REQUEST_CODE);
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
                        }
                        dao.delete(imageInfo.getId());
//                        Lu.i(imageInfo.getId()+"");
                        deleteNum++;
                    }
                    Toast.makeText(Camera03Activity.this, "清空了" + deleteNum + "个文件", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断网络联机
                Boolean netFlag = NetUtils.isNetworkAvailable(getApplicationContext());
                if (netFlag) {
                } else {
                    Toast.makeText(Camera03Activity.this, "无法连接到服务器，请检查网络设置", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断上传本地和云端
                boolean flag_local;
                boolean flag_cloud;
                if (sw_local.isChecked()) {
                    flag_local = true;
                } else {
                    flag_local = false;
                }
                if (sw_cloud.isChecked()) {
                    flag_cloud = true;
                } else {
                    flag_cloud = false;
                }
                String UPLOADFILE_PATH;
                final String IMAGE_INFO_PATH;
                //设置服务器状态
                SharedPreferences spf = getSharedPreferences("xtools", MODE_PRIVATE);
                SharedPreferences.Editor editor = spf.edit();
                if (flag_cloud) {
                    UPLOADFILE_PATH = Constant.UPLOADFILE_PATH_CLOUD;
                    IMAGE_INFO_PATH = Constant.IMAGE_INFO_PATH_CLOUD;
                    editor.putString("SERVER_STATUS", Constant.SERVER_STATUS_CLOUD);
                } else {
                    UPLOADFILE_PATH = Constant.UPLOADFILE_PATH_LOCAL;
                    IMAGE_INFO_PATH = Constant.IMAGE_INFO_PATH_LOCAL;
                    editor.putString("SERVER_STATUS", Constant.SERVER_STATUS_LOCAL);
                }
                editor.commit();

                if (fileDir != null && fileDir.isDirectory()) {
                    final ImageInfoDao dao = new ImageInfoDao(Camera03Activity.this);
                    List<ImageInfo> imageLists = dao.findAll();
                    final int num = imageLists.size();
                    Lu.i("数据库读取上传文件的个数：" + num);
                    for (int i = 0; i < num; i++) {
                        final ImageInfo imageInfo = imageLists.get(i);
                        final File phoneFile = new File(fileDir, imageInfo.getFileName());
                        if (phoneFile.exists()) {
                            Bitmap bmp = FileUtils.getBmp(phoneFile);
                            final File compressFile = new File(fileDir, phoneFile.getName());
                            //判断服务器状态，设置合适压缩率
                            String server_status = spf.getString("SERVER_STATUS", Constant.SERVER_STATUS_CLOUD);
                            FileUtils.compressBmpToFile(bmp, compressFile, Camera03Activity.this, server_status);
                            //上传图片
                            NetUtils.uploadByAsyncHttpClient(compressFile, UPLOADFILE_PATH, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    Lu.i(compressFile.getName() + " 上传成功");
                                    //手机文件删除
                                    phoneFile.delete();
                                    //本机数据库删除记录
                                    dao.delete(imageInfo.getId());
                                    //上传信息到服务器
                                    Gson gson = new Gson();
                                    String data = gson.toJson(imageInfo);
                                    Lu.i(data);
                                    NetUtils.sendJson(data, IMAGE_INFO_PATH, "data");
                                }
                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                    StringBuffer sb = new StringBuffer();
                                    Header header = null;
                                    for (int j = 0; j < headers.length; j++) {
                                        header = headers[j];
                                        String str = header.getName() + ":" + header.getValue() + " ";
                                        sb.append(str);
                                    }
                                    Lu.i("文件上传失败 " + sb.toString());
                                }
                                @Override
                                public void onProgress(long bytesWritten, long totalSize) {
                                    super.onProgress(bytesWritten, totalSize);
                                }
                            });
                        }
                    }
//                    //上传信息到服务器
//                    Gson gson = new Gson();
//                    String datas = gson.toJson(imageLists);
//                    Lu.i(datas);
//                    NetUtils.sendJson(datas, Constant.IMAGE_INFO_PATH, "datas");
//                    //清除缓存
//                    //imageLists.clear();
                    Toast.makeText(Camera03Activity.this, num + "个文件已上传", Toast.LENGTH_LONG).show();
                }
            }
        });


//        btn_compressRate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int fileSize = Integer.parseInt(et_compressRate.getText().toString());
//                SharedPreferences spf = getSharedPreferences("xtools", MODE_PRIVATE);
//                SharedPreferences.Editor editor = spf.edit();
//                editor.putInt("fileSize", fileSize * 1000);
//                editor.commit();
//                Toast.makeText(Camera03Activity.this, "" + fileSize, Toast.LENGTH_SHORT).show();
//            }
//        });
        sw_local = (Switch) findViewById(R.id.sw_local);
        sw_cloud = (Switch) findViewById(R.id.sw_cloud);
        sw_local.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sw_cloud.setChecked(false);
                } else {
                    sw_cloud.setChecked(true);
                }
            }
        });
        sw_cloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sw_local.setChecked(false);
                } else {
                    sw_local.setChecked(true);
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mItems);
        sp.setAdapter(adapter);
    }

    public void openCamera(File imageFile, int cameraRequestCode) {
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");

        Uri fileUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, cameraRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageInfoDao dao = new ImageInfoDao(this);
        //本机数据库登入图片信息
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setFileName(imageFile.getName());
        imageInfo.setDate(DateUtils.getDateStr("yyyy-MM-dd HH:mm:ss"));
        String comments = et_comments.getText().toString();
        if (TextUtils.isEmpty(comments)) {
            imageInfo.setComments("正常");
        } else {
            imageInfo.setComments(comments);
        }
        String type = sp.getSelectedItem().toString();
        imageInfo.setType(type);
        imageInfo.setUserName("宋石磊");
        Lu.i(imageInfo.toString());
        dao.add(imageInfo);
    }
}

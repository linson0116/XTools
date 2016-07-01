package com.linson.xtools.app03.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.linson.xtools.R;
import com.linson.xtools.utils.Constant;
import com.linson.xtools.utils.FileUtils;
import com.linson.xtools.utils.Lu;
import com.linson.xtools.utils.NetUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Camera03FileListActivity extends AppCompatActivity {
    File[] files = null;
    List<String> dataList = null;
    BaseAdapter adapter = new FileListAdapter();
    String tv_FileName = "";
    private ListView lv_cameraFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera03_file_list);
        init();
    }

    private void init() {

        File fileDir = getExternalFilesDir("");
        if (fileDir.isDirectory()) {
            files = fileDir.listFiles();
            dataList = new ArrayList<String>();
            for (int i = 0; i < files.length; i++) {
                dataList.add(files[i].getName());
            }
        }
        lv_cameraFileList = (ListView) findViewById(R.id.lv_cameraFileList);
        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);

        lv_cameraFileList.setAdapter(adapter);
    }

    class FileListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            tv_FileName = dataList.get(position);
            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.activity_camera03_list_file_item, null);
            }

            final TextView tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);

            tv_fileName.setText(tv_FileName);
            Lu.i(tv_FileName);
            Button btn_upload = (Button) view.findViewById(R.id.btn_upload);

            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ViewParent vp = v.getParent().getParent();
                    LinearLayout ll = (LinearLayout) vp;
                    final TextView tv = (TextView) ll.findViewById(R.id.tv_fileName);
                    final String uploadFileName = tv.getText().toString();
                    Lu.i(uploadFileName);
                    final File file = new File(getExternalFilesDir(""), uploadFileName);
                    if (file.exists()) {
                        Lu.i("上传文件存在");
                        Bitmap bmp = FileUtils.getBmp(file);
                        final File compressFile = new File(getExternalFilesDir(""), "C_" + uploadFileName);
                        FileUtils.compressBmpToFile(bmp, compressFile);
                        //上传图片
                        NetUtils.uploadByAsyncHttpClient(compressFile, Constant.UPLOADFILE_PATH, new AsyncHttpResponseHandler() {
                            ViewParent vp = v.getParent().getParent().getParent();
                            LinearLayout ll = (LinearLayout) vp;
                            ProgressBar progressBar = (ProgressBar) ll.findViewById(R.id.pb);

                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                Lu.i("文件上传成功");
                                if (file.exists()) {
                                    file.delete();
                                    Lu.i("源文件删除");
                                }
                                if (compressFile.exists()) {
                                    compressFile.delete();
                                    Lu.i("压缩文件删除");
                                }
                                if (dataList.contains(uploadFileName)) {
                                    dataList.remove(uploadFileName);
                                    Lu.i("文件列表中移除已上传文件");
                                }
                                //adapter.notifyDataSetChanged();
                                //上传成功后，上传按钮不可用
                                v.setEnabled(false);
                                Toast.makeText(Camera03FileListActivity.this, "文件上传成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                Lu.i("文件上传失败");
                            }

                            @Override
                            public void onProgress(long bytesWritten, long totalSize) {
                                Lu.i("bytesWritten=" + bytesWritten + " " + "totalSize=" + totalSize);
                                progressBar.setMax((int) totalSize);
                                progressBar.setProgress((int) bytesWritten);
                                super.onProgress(bytesWritten, totalSize);
                            }
                        });

                    }
                }
            });
            return view;
        }
    }
}

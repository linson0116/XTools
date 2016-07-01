package com.linson.xtools.app03.activity;

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
import com.linson.xtools.app03.dao.ImageInfoDao;
import com.linson.xtools.app03.domain.ImageInfo;
import com.linson.xtools.utils.Constant;
import com.linson.xtools.utils.Lu;
import com.linson.xtools.utils.NetUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.util.List;

public class Camera03FileListActivity extends AppCompatActivity {
    File[] files = null;
    //List<String> dataList = null;
    List<ImageInfo> imageList = null;
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
//        File fileDir = getExternalFilesDir("");
//        if (fileDir.isDirectory()) {
//            files = fileDir.listFiles();
//            dataList = new ArrayList<String>();
//            for (int i = 0; i < files.length; i++) {
//                dataList.add(files[i].getName());
//            }
//        }
        ImageInfoDao dao = new ImageInfoDao(this);
        imageList = dao.findAll();

        lv_cameraFileList = (ListView) findViewById(R.id.lv_cameraFileList);
        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        lv_cameraFileList.setAdapter(adapter);
    }

    class FileListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageList.size();
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
            final ImageInfo imageInfo = imageList.get(position);

            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.activity_camera03_list_file_item, null);
            }

            TextView tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            tv_fileName.setText(imageInfo.getFileName());

            TextView tv_fileType = (TextView) view.findViewById(R.id.tv_fileType);
            tv_fileType.setText(imageInfo.getType());

            TextView tv_comments = (TextView) view.findViewById(R.id.tv_comments);
            tv_comments.setText(imageInfo.getComments());

            Lu.i(tv_FileName);
            Button btn_upload = (Button) view.findViewById(R.id.btn_upload);
            //图片文件上传
            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ViewParent vp = v.getParent().getParent();
                    LinearLayout ll = (LinearLayout) vp;
                    final TextView tv = (TextView) ll.findViewById(R.id.tv_fileName);
                    final String uploadFileName = tv.getText().toString();
                    Lu.i(uploadFileName);
                    final ImageInfoDao dao = new ImageInfoDao(Camera03FileListActivity.this);
                    final ImageInfo uploadImageInfo = dao.findByFileName(uploadFileName);
                    if (uploadImageInfo != null) {
                        final File file = new File(getExternalFilesDir(""), uploadFileName);
                        if (file.exists()) {
                            Lu.i("上传文件存在");
                            //上传图片
                            NetUtils.uploadByAsyncHttpClient(file, Constant.UPLOADFILE_PATH, new AsyncHttpResponseHandler() {
                                ViewParent vp = v.getParent().getParent().getParent();
                                LinearLayout ll = (LinearLayout) vp;
                                ProgressBar progressBar = (ProgressBar) ll.findViewById(R.id.pb);

                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    Lu.i("文件上传成功");
                                    if (file.exists()) {
                                        file.delete();
                                        dao.delete(uploadImageInfo.getId());
                                        Lu.i("源文件删除");
                                    }
//                                    if (imageList.contains(uploadImageInfo)) {
//                                        imageList.remove(uploadImageInfo);
//                                        Lu.i("文件列表中移除已上传文件");
//                                    }
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
                                    progressBar.setVisibility(ProgressBar.VISIBLE);
                                    progressBar.setMax((int) totalSize);
                                    progressBar.setProgress((int) bytesWritten);
                                    super.onProgress(bytesWritten, totalSize);
                                }
                            });
                        }
                    }
                }
            });
            return view;
        }
    }
}

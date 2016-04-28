package com.linson.xtools.app02.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.linson.xtools.R;
import com.linson.xtools.app02.domain.SmsInfo;
import com.linson.xtools.app02.utils.XmlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Sms02Activity extends AppCompatActivity {
    SmsAdapter adapter;
    List<SmsInfo> smsList = new ArrayList<SmsInfo>();
    private ListView lv;
    private String TAG = "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms02);

        File file = new File(getFilesDir(), "smss.xml");
        if (!file.exists()) {
            XmlUtils.createSmsInfo(file, new String[]{"test", "test", "test"});
        }
        try {
            InputStream is = new FileInputStream(file);
            smsList = XmlUtils.getSmsList(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        lv = (ListView) findViewById(R.id.lv_sms);
        adapter = new SmsAdapter();
        lv.setAdapter(adapter);
    }

    class SmsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return smsList.size();
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
            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.activity_sms02_list_item, null);
            }
            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
            TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
            TextView tv_body = (TextView) view.findViewById(R.id.tv_body);
            SmsInfo smsInfo = smsList.get(position);
            tv_number.setText(smsInfo.getNumber());
            tv_date.setText(smsInfo.getDate());
            tv_body.setText(smsInfo.getBody());
            return view;
        }
    }
}

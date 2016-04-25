package com.linson.xtools.app01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.linson.xtools.R;
import com.linson.xtools.app01.dao.PhoneTemplateDao;
import com.linson.xtools.app01.domain.PhoneTemplate;

import java.util.ArrayList;
import java.util.List;

public class ListTemplateActivity extends AppCompatActivity {
    List<PhoneTemplate> list = new ArrayList<PhoneTemplate>();
    ListView lv = null;
    TemplateAdapter adapter = null;
    PhoneTemplateDao dao = null;
    Menu menu = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_template);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        //Log.i("log", list.size()+"");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void init() {
        dao = new PhoneTemplateDao(this);
        lv = (ListView) findViewById(R.id.lv_template);
        MenuItem item = (MenuItem) findViewById(R.id.item_btn_delete);
        //item.setVisible(true);

        adapter = new TemplateAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneTemplate template = list.get(position);
                //hot 点击+1
                int hot = template.getHot();
                hot++;
                template.setHot(hot);
                dao.update(template);
                //传值
                Intent intent = new Intent();
                intent.putExtra("number", template.getNumber());
                intent.putExtra("content", template.getContent());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        list = dao.findAll();
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        menu.findItem(R.id.item_btn_delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.item_add) {
            Log.i("log", "add");
            Intent intent = new Intent(getApplicationContext(),AddTemplateActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.item_delete) {
            menu.findItem(R.id.item_btn_delete).setVisible(true);
            return true;
        } else if (id == R.id.item_update) {
            return true;
        } else if (id == R.id.item_btn_delete) {
            Log.i("log", "onOptionsItemSelected: item_btn_delete");
        }

        return super.onOptionsItemSelected(item);
    }

    class TemplateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.list_template_item, null);
            } else {
                view = convertView;
            }
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            TextView tv_hot = (TextView) view.findViewById(R.id.tv_hot);
            PhoneTemplate phoneTemplate = list.get(position);
            Log.i("log", "getView: " + phoneTemplate);
            tv_name.setText(phoneTemplate.getName());
            tv_number.setText(phoneTemplate.getNumber() + "");
            tv_content.setText(phoneTemplate.getContent());
            tv_hot.setText(phoneTemplate.getHot() + "");
            return view;
        }
    }

}

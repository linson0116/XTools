package com.linson.xtools.app01.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.linson.xtools.utils.Lu;

import java.util.ArrayList;
import java.util.List;

public class ListTemplateActivity extends AppCompatActivity {
    List<PhoneTemplate> phoneTemplateList = new ArrayList<PhoneTemplate>();
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
        phoneTemplateList = dao.findAll();
        lv = (ListView) findViewById(R.id.lv_template);
        MenuItem item = (MenuItem) findViewById(R.id.item_btn_delete);
        adapter = new TemplateAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PhoneTemplate template = phoneTemplateList.get(position);
                //hot 点击+1
                int hot = template.getHot();
                hot++;
                template.setHot(hot);
                dao.update(template);
                //传值返回
                Intent intent = new Intent();
                intent.putExtra("number", template.getNumber());
                intent.putExtra("content", template.getContent());
                setResult(RESULT_OK, intent);
                finish();
                Lu.i("click item = " + template.getName() + " " + template.getNumber() + " " + template.getContent());
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ListTemplateActivity.this);
                dialog.setMessage("删除这一条记录？");
                dialog.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhoneTemplate p = phoneTemplateList.get(position);
                        dao.delete(p.getId());
                        phoneTemplateList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        phoneTemplateList = dao.findAll();
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
            Intent intent = new Intent(getApplicationContext(), AddTemplateActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.item_delete) {
            //进入删除状态 多选删除未完成
            return true;
        } else if (id == R.id.item_update) {
            return true;
        } else if (id == R.id.item_btn_delete) {
            //删除cb选中的条目
        }

        return super.onOptionsItemSelected(item);
    }

    class TemplateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return phoneTemplateList.size();
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
            //checkbox 未完成
            PhoneTemplate phoneTemplate = phoneTemplateList.get(position);
            tv_name.setText(phoneTemplate.getName());
            tv_number.setText(phoneTemplate.getNumber() + "");
            tv_content.setText(phoneTemplate.getContent());
            tv_hot.setText(phoneTemplate.getHot() + "");

            return view;
        }
    }

}

package com.linson.xtools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.linson.xtools.app01.activity.CallActivity;
import com.linson.xtools.app02.activity.Sms02Activity;
import com.linson.xtools.app03.activity.Camera03Activity;
import com.linson.xtools.app04.activity.Screen04Activity;
import com.linson.xtools.app05.activity.Login05Activity;
import com.linson.xtools.app09.activity.Test09Activity;
import com.linson.xtools.utils.Lu;
import com.linson.xtools.utils.PermissionUtils;
import com.linson.xtools.utils.SpUtils;

public class MainActivity extends Activity {
    private static final String[] icon_names = {"短信电话", "短信记录", "拍照上传", "屏幕开关", "登录", "", "", "", "测试"};
    private static final int[] icon = {R.drawable.icon_1, R.drawable.icon_2, R.drawable.icon_3, R.drawable.icon_4, R.drawable.icon_5, R.drawable.icon_6, R.drawable.icon_7, R.drawable.icon_8, R.drawable.icon_9};
    private GridView gv;
    private RefreshUserInfoReceiver refreshUserInfoReceiver;
    private TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Lu.i("软件启动 onCreate");
        initUI();
        initData();
    }

    private void initData() {
        //初始化登录状态
        boolean isLogin = SpUtils.getBoolean(this, "login_status");
        if (isLogin) {
            String username = SpUtils.getString(this, "username");
            tv_username.setText(username);
        } else {
            tv_username.setText("未登录");
        }
    }

    private void initUI() {
        //文字闪烁
        ShimmerFrameLayout shimmer = (ShimmerFrameLayout) findViewById(R.id.shimmer_frame_layout);
        shimmer.setDuration(3000);
        shimmer.startShimmerAnimation();
        //
        gv = (GridView) findViewById(R.id.gv);
        GridAdapter gridAdapter = new GridAdapter();
        gv.setAdapter(gridAdapter);
        //设置图标飞入动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_icon);
        LayoutAnimationController animationController = new LayoutAnimationController(animation);
        gv.setLayoutAnimation(animationController);
        animationController.start();
        //注册刷新用户信息的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refresh_user_info");
        refreshUserInfoReceiver = new RefreshUserInfoReceiver();
        registerReceiver(refreshUserInfoReceiver, intentFilter);
        //
        tv_username = (TextView) findViewById(R.id.tv_username);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Lu.i("点击按钮1 短信电话");
                        intent = new Intent(getApplicationContext(), CallActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Lu.i("点击按钮2 短信记录");
                        intent = new Intent(getApplicationContext(), Sms02Activity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Lu.i("点击按钮3 拍照上传");
                        intent = new Intent(getApplicationContext(), Camera03Activity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        Lu.i("点击按钮4 屏幕解锁");
                        intent = new Intent(getApplicationContext(), Screen04Activity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        Lu.i("点击按钮5 登录");
                        intent = new Intent(getApplicationContext(), Login05Activity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        Lu.i("点击按钮9 测试");
                        intent = new Intent(getApplicationContext(), Test09Activity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        PermissionUtils.verifyStoragePermissions(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(refreshUserInfoReceiver);
    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icon_names.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.list_item_main_icon, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            iv.setImageResource(icon[position]);
            tv.setText(icon_names[position]);
            return view;
        }
    }

    class RefreshUserInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String username = intent.getStringExtra("username");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_username.setText(username);
                }
            });
        }
    }
}

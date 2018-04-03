package com.my_music.Acitity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.my_music.Adapter.ViewPagerAdapter;
import com.my_music.R;
import com.my_music.Service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActity implements MusicService.Callbcak {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> list;
    private String[] mTab = {"今日推荐∨", "我的关注∨"};
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            Intent intent =new Intent(MainActivity.this,MusicService.class);
            stopService(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
        list = new ArrayList<Fragment>();
        list.add(new Fragment_tuijian());
        list.add(new Fragment_guanzhu());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), list, mTab);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}

package com.my_music.Theme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.my_music.Acitity.BaseActity;
import com.my_music.Adapter.ViewPagerAdapter;
import com.my_music.Image.ImageLoader;
import com.my_music.R;
import com.my_music.Service.MusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MXY on 2018/3/19.
 */

public class Themes extends BaseActity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> list;
    private String[] mTab = {"播放列表", "相似歌单"};
    private TextView title, time;
    private ImageView imageView;
    private Button fh;
    private MusicService musicService;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_theme);
        fullScreen(this);
        title = findViewById(R.id.tv_title);
        imageView = findViewById(R.id.im_image);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
        time = findViewById(R.id.tv_time);


        fh = findViewById(R.id.bt_fh);
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String zt = getIntent().getExtras().getString("zt");
        String title1 = getIntent().getExtras().getString("title");
        String image = getIntent().getExtras().getString("image");
        String time1 = getIntent().getExtras().getString("time");
        new ImageLoader().showIamgeByThread(imageView, image);
        time.setText("创建时间:" + time1);
        title.setText(title1);
        list = new ArrayList<>();
        Theme_gequ theme_gequ = new Theme_gequ(zt);
        list.add(theme_gequ);
        list.add(new Theme_xiangsi());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), list, mTab);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}

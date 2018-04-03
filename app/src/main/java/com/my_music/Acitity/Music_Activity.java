package com.my_music.Acitity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.my_music.Beans.Bean;
import com.my_music.Image.ImageLoader;
import com.my_music.R;
import com.my_music.Service.MusicService;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by MXY on 2018/3/22.
 */

public class Music_Activity extends AppCompatActivity implements View.OnClickListener, MusicService.Callbcak {
    private Button fh;
    private TextView state, playing_time, max_time;
    private SeekBar seekbar;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private MusicService musicService;
    private ImageView imageView, play_pauseBtn, play_up, play_down;
    private Animation animation;
    private String name, singer, image, src;
    private TextView music_name, music_singer;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder) iBinder).getService();
            musicService.addCallback(Music_Activity.this);
            Log.e("musicService", musicService + "");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };


    @Override
    public void getMusicService(MusicService usicService, List<Bean> fruits, int position) {
        this.name = fruits.get(position).getMusic_name();
        this.singer = fruits.get(position).getMusic_singer();
        this.image = fruits.get(position).getMusic_image();
        this.src = fruits.get(position).getMusic_src();
        music_name.setText(name);
        music_singer.setText("一" + singer + "一");
        if (!musicService.ismediaPlayer) {
            return;
        }
        seekbar.setProgress(musicService.mediaPlayer.getCurrentPosition());//设置seekbar 为当前歌曲播放到的时间进度
        seekbar.setMax(musicService.mediaPlayer.getDuration());
        playing_time.setText(time.format(musicService.mediaPlayer.getCurrentPosition()));
        max_time.setText(time.format(musicService.mediaPlayer.getDuration()));
        new ImageLoader().showIamgeByThread(imageView, image);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.music_activity);
        fullScreen(Music_Activity.this);

        music_name = findViewById(R.id.tv_music_name);
        music_singer = findViewById(R.id.tv_music_singer);
        play_pauseBtn = (ImageView) findViewById(R.id.play_pause);//开始/暂停按钮
        play_up = (ImageView) findViewById(R.id.play_up);//上一曲
        play_down = (ImageView) findViewById(R.id.play_down);//下一曲
        state = (TextView) findViewById(R.id.state);//seekbar上面表示状态的文字textview
        playing_time = (TextView) findViewById(R.id.playing_time);//seekbar左边现在播放的时间
        max_time = (TextView) findViewById(R.id.song_time);//播放的这首歌曲的时长
        seekbar = (SeekBar) findViewById(R.id.seekbar);//seekbar
        imageView = findViewById(R.id.imageview);

        //右上角返回事件
        fh = findViewById(R.id.bt_fh);
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        animation = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        imageView.startAnimation(animation);


        play_pauseBtn.setOnClickListener(this);
        play_up.setOnClickListener(this);
        play_down.setOnClickListener(this);

        //绑定后台事物
        Intent intent = new Intent(Music_Activity.this, MusicService.class);
        bindService(intent, sc, BIND_ABOVE_CLIENT);//綁定服務

        //设置seekbar到哪里，音乐到哪里
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    musicService.mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_up:
                musicService.paly_up();
                break;
            case R.id.play_pause:
                musicService.playORpuase();
                if (musicService.mediaPlayer.isPlaying()) {
                    state.setText("Playing");//当音乐在播放时，我们设置state的内容为Playing
                    play_pauseBtn.setBackgroundResource(R.drawable.desk_pause);
                } else {
                    state.setText("Paused");//当音乐停止时，设置state内容为Paused
                    play_pauseBtn.setBackgroundResource(R.drawable.desk_play);
                }
                break;
            case R.id.play_down:
                musicService.paly_down();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //去掉状态栏
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sc);
    }
}

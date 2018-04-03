package com.my_music.Acitity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.my_music.Beans.Bean;
import com.my_music.Image.ImageLoader;
import com.my_music.R;
import com.my_music.Service.MusicService;

import java.util.List;

/**
 * Created by MXY on 2018/3/22.
 */

public class BaseActity extends AppCompatActivity implements MusicService.Callbcak {

    private WindowManager windowManager;
    /**
     * 根视野
     */
    private FrameLayout mContentContainer;
    /**
     * 浮动视野
     */
    private View mFloatView;

    private MusicService musicService;
    private TextView music_name, music_singer;
    private ImageView imageView_z, play_pause, play_down;
    private SeekBar seekbar;
    private Animation animation;
    private String name, singer, image, src;

    @Override

    public void getMusicService(MusicService usicService, List<Bean> fruits, int position) {
        if (fruits == null) {
            return;
        }
        this.name = fruits.get(position).getMusic_name();
        this.singer = fruits.get(position).getMusic_singer();
        this.image = fruits.get(position).getMusic_image();
        this.src = fruits.get(position).getMusic_src();
        this.musicService = usicService;
        if (!musicService.ismediaPlayer) {
            return;
        }
        seekbar.setProgress(musicService.mediaPlayer.getCurrentPosition());//设置seekbar 为当前歌曲播放到的时间进度
        seekbar.setMax(musicService.mediaPlayer.getDuration());
        if (image == null) {
            return;
        }
        new ImageLoader().showIamgeByThread(imageView_z, image);
        music_name.setText(name);
        music_singer.setText(singer);
    }


    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder) iBinder).getService();
            musicService.addCallback(BaseActity.this);
            Log.e("musicService", musicService + "");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewGroup mDecorView = (ViewGroup) getWindow().getDecorView();
        mContentContainer = (FrameLayout) ((ViewGroup) mDecorView.getChildAt(0)).getChildAt(1);
        mFloatView = LayoutInflater.from(getBaseContext()).inflate(R.layout.music_playbar, null);
        imageView_z = mFloatView.findViewById(R.id.imageview_z);//旋转图片
        music_name = mFloatView.findViewById(R.id.tv_music_name);
        music_singer = mFloatView.findViewById(R.id.tv_music_singer);
        play_pause = mFloatView.findViewById(R.id.play_pause);//开始/暂停按钮
        play_down = mFloatView.findViewById(R.id.play_down);//下一首
        seekbar = mFloatView.findViewById(R.id.seekbar);

        mFloatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否能进行跳转！
                if (musicService.ismediaPlayer) {
                    Intent intent = new Intent(BaseActity.this, Music_Activity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("singer", singer);
                    intent.putExtra("image", image);
                    intent.putExtra("src", src);
                    startActivity(intent);
                }
            }
        });


        Intent service = new Intent(BaseActity.this, MusicService.class);
        bindService(service, sc, BaseActity.BIND_AUTO_CREATE);


        animation = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        imageView_z.startAnimation(animation);

        play_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.paly_down();
            }
        });


        play_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                musicService.playORpuase();
                if (musicService.mediaPlayer.isPlaying()) {
                    play_pause.setBackgroundResource(R.drawable.playbar_btn_pause);
                } else {
                    play_pause.setBackgroundResource(R.drawable.playbar_btn_play);
                }
            }
        });

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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        mContentContainer.addView(mFloatView, layoutParams);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /***
     * 重点，设置这个可以实现前进Activity时候的无动画切换
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//设置切换没有动画，用来实现活动之间的无缝切换
        super.startActivity(intent);
    }

    /**
     * 重点，在这里设置按下返回键，或者返回button的时候无动画
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);//设置返回没有动画
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sc);
    }
}
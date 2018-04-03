package com.my_music.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.my_music.Beans.Bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MXY on 2018/3/20.
 */

public class MusicService extends Service {

    public MediaPlayer mediaPlayer = new MediaPlayer();

    //回调接口的集合
    private List<Callbcak> list;
    private List<Bean> fruits;
    private int position;
    private String name, singer, image, src;
    public boolean ismediaPlayer = false;

    public interface Callbcak {
        void getMusicService(MusicService usicService, List<Bean> fruits, int position);
    }

    public void addCallback(Callbcak callbcak) {
        list.add(callbcak);
    }


    public void addData(List<Bean> fruits) {
        this.fruits = fruits;
    }

    public MusicService() {

    }

    private IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;//找到后台服务的指针，返回后台服务实例
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("创建服务成功:", "成功！");
        mHandler.post(mRunnable);
        list = new ArrayList<>();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("创建服务成功!!!:", "成功！");
        name = intent.getStringExtra("name");
        singer = intent.getStringExtra("singer");
        image = intent.getStringExtra("image");
        src = intent.getStringExtra("src");
        position = Integer.parseInt(intent.getStringExtra("position"));
        Log.e("第几个", position + "");
        if (src == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();//停止播放音乐
            mediaPlayer.reset();
        }
        try {
            ismediaPlayer = true;
            mediaPlayer.setDataSource(src);
            mediaPlayer.prepare();//进入就绪状态
            mediaPlayer.start();
            mediaPlayer.setLooping(true); //设置循环
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();//停止播放音乐
            mediaPlayer.release();//释放mediaPlayer资源
        }
    }

    public void playORpuase() {
        Log.e("判断", mediaPlayer.isPlaying() + "");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();//音乐暂停
        } else {
            mediaPlayer.start();//音乐开始
        }
    }

    public void paly_up() {
        if (position == 0) {
            position = fruits.size() - 1;
        } else {
            --position;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();//停止播放音乐
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(fruits.get(position).getMusic_src());
            mediaPlayer.prepare();//进入就绪状态
            mediaPlayer.start();
            mediaPlayer.setLooping(true); //设置循环
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void paly_down() {
        if (position == fruits.size() - 1) {
            position = 0;
        } else {
            position++;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();//停止播放音乐
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(fruits.get(position).getMusic_src());
            mediaPlayer.prepare();//进入就绪状态
            mediaPlayer.start();
            mediaPlayer.setLooping(true); //设置循环
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).getMusicService(MusicService.this, fruits, position);
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };


    @Override
    public boolean onUnbind(Intent intent) {
        ismediaPlayer = false;
        return super.onUnbind(intent);
    }
}
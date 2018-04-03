package com.my_music.Theme;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.my_music.Acitity.BaseActity;
import com.my_music.Adapter.ListViewAdapter;
import com.my_music.Beans.Bean;
import com.my_music.HttpData;
import com.my_music.R;
import com.my_music.Service.MusicService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MXY on 2018/3/19.
 */

@SuppressLint("ValidFragment")
public class Theme_gequ extends Fragment {

    private String mTheme;
    private ListView listView;
    private ArrayList<Bean> fruits;


    public Theme_gequ(String theme) {
        this.mTheme = theme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.theme_gequ, container, false);
        listView = view.findViewById(R.id.lv_show);
        new Thread(mRunnable).start();
        return view;
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            String data = HttpData.Theme(mTheme);
            try {
                JSONArray jsonArray = new JSONArray(data);
                fruits = new ArrayList<Bean>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String music_name = jsonObject.getString("music_name");
                    String music_singer = jsonObject.getString("music_singer");
                    String music_image = jsonObject.getString("music_image");
                    String music_src = jsonObject.getString("music_src");
                    Bean bean = new Bean();
                    bean.setMusic_name(music_name);
                    bean.setMusic_singer(music_singer);
                    bean.setMusic_image(music_image);
                    bean.setMusic_src(music_src);
                    fruits.add(bean);
                }
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("key", fruits);
                message.setData(bundle);
                mHandler.sendMessage(message);
            } catch (JSONException e) {
                Log.e("错误2", e.toString());
            }
        }
    };


    private MusicService musicService;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder) iBinder).getService();
            musicService.addCallback((MusicService.Callbcak) getActivity());
            musicService.addData(fruits);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            final List<Bean> fruits = (ArrayList<Bean>) message.getData().getSerializable("key");
            final ListViewAdapter adapter = new ListViewAdapter(getActivity(), fruits);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    adapter.setCurrentItem(position);
                    adapter.setClick(true);
                    adapter.notifyDataSetChanged();
                    Intent service = new Intent(getActivity(), MusicService.class);
                    service.putExtra("name", fruits.get(position).getMusic_name());
                    service.putExtra("singer", fruits.get(position).getMusic_singer());
                    service.putExtra("src", fruits.get(position).getMusic_src());
                    service.putExtra("image", fruits.get(position).getMusic_image());
                    service.putExtra("position", position + "");
                    getActivity().startService(service);
                    getActivity().bindService(service, sc, BaseActity.BIND_AUTO_CREATE);
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(sc);
    }
}

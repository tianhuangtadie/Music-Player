package com.my_music.Acitity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my_music.Adapter.RecyclerViewAdapter;
import com.my_music.Beans.ThemeBean;
import com.my_music.HttpData;
import com.my_music.R;
import com.my_music.Theme.Themes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MXY on 2018/3/18.
 */

public class Fragment_tuijian extends Fragment {

    private static final String TAG = "Fragment_tuijian";
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<ThemeBean> fruits;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tuijian, container, false);
        mRecyclerView = rootView.findViewById(R.id.RecyclerView);
        new Thread(mRunnable).start();
        return rootView;
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            String data = HttpData.Query();
            try {
                JSONArray jsonArray = new JSONArray(data);
                fruits = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String music_zt = jsonObject.getString("music_zt");
                    String music_zt_title = jsonObject.getString("music_zt_title");
                    String music_zt_image = jsonObject.getString("music_zt_image");
                    String music_zt_time = jsonObject.getString("music_zt_time");
                    ThemeBean bean = new ThemeBean();
                    bean.setMusic_zt(music_zt);
                    bean.setMusic_zt_title(music_zt_title);
                    bean.setMusic_zt_image(music_zt_image);
                    bean.setMusic_zt_time(music_zt_time);
                    fruits.add(bean);
                }
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ksy", fruits);
                message.setData(bundle);
                mHandler.sendMessage(message);
            } catch (JSONException e) {
                Log.e("错误2", e.toString());
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            final ArrayList<ThemeBean> fruits = (ArrayList<ThemeBean>) message.getData().getSerializable("ksy");
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mRecyclerViewAdapter = new RecyclerViewAdapter(fruits);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
            mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(getActivity(), Themes.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("zt", fruits.get(position).getMusic_zt());
                    bundle.putSerializable("title", fruits.get(position).getMusic_zt_title());
                    bundle.putSerializable("image", fruits.get(position).getMusic_zt_image());
                    bundle.putSerializable("time", fruits.get(position).getMusic_zt_time());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    };

}

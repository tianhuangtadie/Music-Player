package com.my_music.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my_music.Beans.Bean;
import com.my_music.Service.MusicService;
import com.my_music.R;

import java.util.List;


/**
 * Created by MXY on 2018/3/19.
 */

public class ListViewAdapter extends BaseAdapter implements MusicService.Callbcak {

    private int position1 ;
    private Context mcontext;
    private LayoutInflater inflater;
    private List<Bean> mfruits;
    //当前Item被点击的位置
    private int currentItem = -1;
    private  boolean isClick=false;

    public ListViewAdapter(Context context, List<Bean> fruits) {
        this.mfruits = fruits;
        this.mcontext = context;
        inflater = LayoutInflater.from(mcontext);
    }


    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public void setClick(boolean click){
        this.isClick=click;
    }

    @Override
    public int getCount() {
        return mfruits.size();
    }

    @Override
    public Object getItem(int i) {
        return mfruits.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void getMusicService(MusicService usicService, List<Bean> fruits, int position) {
        this.position1=position;
    }


    public static class ViewHolder {
        private TextView music_name, music_singer,tt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            holder.music_name = convertView.findViewById(R.id.tv_music_name);
            holder.music_singer = convertView.findViewById(R.id.tv_music_singer);
            holder.tt = convertView.findViewById(R.id.tv_tt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String aa = mfruits.get(position).getMusic_name();
        String bb = mfruits.get(position).getMusic_singer();
        holder.music_name.setText(aa);
        holder.music_singer.setText(bb);

        if (currentItem == position &&isClick) {
            //如果被点击，设置当前TextView被选中
            holder.music_name.setSelected(true);
            holder.music_singer.setSelected(true);
            holder.tt.setSelected(true);
            holder.tt.setBackgroundColor(Color.parseColor("#2798e7"));
        } else {
            //如果没有被点击，设置当前TextView未被选中
            holder.music_name.setSelected(false);
            holder.music_singer.setSelected(false);
            holder.tt.setSelected(false);
            holder.tt.setBackgroundColor(Color.parseColor("#eeeeee"));
        }




        return convertView;
    }

}

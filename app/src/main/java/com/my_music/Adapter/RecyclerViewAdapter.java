package com.my_music.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.my_music.Beans.ThemeBean;
import com.my_music.Image.ImageLoader;
import com.my_music.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MXY on 2018/3/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ThemeBean> mData;
    private static final String TAG = "RecyclerViewAdapter";
    private RecyclerViewAdapter.OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView zt;

        //初始化viewHolder，此处绑定后在onBindViewHolder中可以直接使用
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.im_gd);
            zt = itemView.findViewById(R.id.tv_zt);
        }
    }

    public RecyclerViewAdapter(ArrayList<ThemeBean> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View views = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(views);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        new ImageLoader().showIamgeByThread(holder.imageView, mData.get(position).getMusic_zt_image());
        holder.zt.setText(mData.get(position).getMusic_zt_title());

        if (mOnItemClickListener != null) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



}

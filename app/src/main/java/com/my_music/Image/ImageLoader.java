package com.my_music.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MXY on 2018/3/15.
 */

public class ImageLoader {

    private ImageView mimageView;
    private String mUrl;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mimageView.setImageBitmap((Bitmap) msg.obj);
        }
    };


    public void showIamgeByThread(ImageView imageView, final String url) {
        mimageView = imageView;
        mUrl = url;
        new Thread() {
            public void run() {
                super.run();
                Bitmap bitmap = getBitmapFromUrl(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    public Bitmap getBitmapFromUrl(String urlString) {
        Bitmap bitmap;
        InputStream up = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            up = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(up);
            urlConnection.disconnect();
            return bitmap;
        } catch (IOException e) {
        } finally {
            try {
                up.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

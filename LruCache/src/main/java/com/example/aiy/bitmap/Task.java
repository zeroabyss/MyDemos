package com.example.aiy.bitmap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * <p>功能简述：后台异步加载图片
 * <p>Created by Aiy on 2017/8/7.
 */

public class Task extends AsyncTask<Integer,Void,Bitmap> {
    private ImageView iv;
    private Main2Activity main2Activity;

    //这里例子是采用网上的,这个设计方法不好,耦合度太高,可以利用接口回调解耦
    public Task(ImageView iv,  Main2Activity main2Activity) {
        this.iv=iv;
        this.main2Activity = main2Activity;
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        //为了体现是task加载的所以就延迟一下
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //就是调用那个缩放Bitmap
        Bitmap bitmap= Main2Activity.getBitmapDecode(main2Activity.getResources(),params[0],200,200);
        //加入缓存
        main2Activity.addBitmap(String.valueOf(params[0]),bitmap);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //如果图片下载成功就显示
        if (bitmap!=null) {
            iv.setImageBitmap(bitmap);
        }
    }
}

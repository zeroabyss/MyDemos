package com.example.aiy.anime;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

/**
 * <p>功能简述：简单的逐帧动画<br> 在Drawable里面建立一个XML属性是Animation-list,添加子项图片和duration间隔时间（毫秒）<br>然后在imageview设置src等于该XML，然后在代码中AnimationDrawable等于imageview的getDrawable()即可。start stop为停止。
 * <p>Created by Aiy on 2017/7/31.
 */

public class FrameAnimation {
    public static void frameAnimation(ImageView v){
        AnimationDrawable ad= (AnimationDrawable) v.getDrawable();
        ad.start();
    }
}

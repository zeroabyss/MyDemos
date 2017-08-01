package com.example.aiy.anime.Property;

import android.view.animation.Interpolator;

/**
 * <p>功能简述：实现一个先减速后加速的interpolator(速度变化控制器)
 * <p>Created by Aiy on 2017/8/1.
 */

public class MyInterpolator implements Interpolator {
    /**
     * 方法简述： 这是必须实现的唯一方法，实现算法然后返回值
     * @param input 这个其实很常见 fraction interpolatedTime是一样的从0开始变化到1 动画的开始和结束
     * 这里是根据加速度来变化的，所以匀速的可以直接return input就可以
     * 其他的要根据算法来实现。
     */
    @Override
    public float getInterpolation(float input) {
        float result;
        //前半部分是正弦的0-90度的变化曲线
        /*if (input<0.5)
            result= (float) (Math.sin(Math.PI *input)/2);
        else{
            //后半是90-180度沿X轴对称的曲线
            result= (float) ((2-Math.sin(Math.PI *input))/2);
        }*/
        result=(2-input)*input;
        return result;
    }
}

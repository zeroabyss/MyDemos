package com.example.aiy.anime;

import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * <p>功能简述：Tween动画的使用，最好是在onWindowFocusChanged中使用而不是在Create()不然获取坐标是错误的，因为还没有初始化完成.<br>
 *
 * <p>Created by Aiy on 2017/7/31.
 */

public class TweenAnimation {
    /**
     * 方法简述： AlphaAnimation的简单使用，功能是透明
     * @param v 把View传进来就可以实现动画效果
     */
    public static void alphaAnimationPlay(View v){
        //参数是透明度，float，0.0f是全透明 1.0是不透明
        AlphaAnimation alpha=new AlphaAnimation(1f,0.1f);
        //以下都是父类Animation的方法

        //这是设置动画持续效果时间
        alpha.setDuration(1000);
        //这是设置结尾停在最后动画效果,停在初始位置alpha.setFillBefore();
        alpha.setFillAfter(true);
        //设置动画重复次数
        alpha.setRepeatCount(5);
        //设置重复的模式，reverse倒序，restart正序
        alpha.setRepeatMode(Animation.REVERSE);
        //这是设置动画开始前的延迟时间，注意如果配合repeat的话每一次都会延迟
        alpha.setStartOffset(2000);
        //使View开始动画
        v.startAnimation(alpha);
    }
    /**
     * 方法简述： RotateAnimation的简单使用，功能是旋转
     * @param v View控件
     * 注意不要在onCreate直接使用，不然中心点总是从0，0开始
     */
    public static void rotate(View v){
        //四个参数其实是三个，第一个是开始的旋转度，第二个是最终位置的旋转，第三第四个是点的XY，是中心点，指从以哪个点为中心来旋转的。
        //第一二个的范围是-360f ~360f的N倍数，可以转N圈
        RotateAnimation rotateAnimation=new RotateAnimation(
                180f,720f,v.getWidth()*2,v.getHeight());
        rotateAnimation.setDuration(2000);
        v.startAnimation(rotateAnimation);
    }

    /**
     * 方法简述： ScaleAnimation的简单使用，功能是扩大缩小
     * @param v 控件View
     * 同样是不推荐使用在OnCreate
     */
    public static void scale(View v){
        //六个参数对应三个值，XY中心点的变化 1.0f为控件默认大小
        //1.X值最初的比例 2.为X的最后比例 34同理为Y
        //56一样是变化的中心点，如果从0，0开始就是为XY扩展 从中心开始就是往四周扩展，也有一个没有56的构造方法默认0，0
        ScaleAnimation sa=new ScaleAnimation(0f,150f,0.0f,200f,0,0);
        sa.setDuration(120);
        sa.setRepeatMode(Animation.RESTART);
        sa.setRepeatCount(20);
        v.startAnimation(sa);
    }
    /**
     * 方法简述： TranslateAnimation的简单应用，是平移功能.
     * 可以从某个点移动到某个点
     */
    public static void translate(View v){

        //四个参数分别为X的初始坐标，X的最终坐标，Y的初始坐标，Y的最终坐标
        TranslateAnimation ta=new TranslateAnimation(100f,1.0f,1.0f,300.0f);
        ta.setDuration(1000);
        v.startAnimation(ta);
    }
    /**
     * 方法简述： AnimationSet是四种效果的集合版
     */
    public static void animeSet(View v){
        Animation animation= AnimationUtils.loadAnimation(v.getContext(),R.anim.dsa);
        v.startAnimation(animation);
    }

}

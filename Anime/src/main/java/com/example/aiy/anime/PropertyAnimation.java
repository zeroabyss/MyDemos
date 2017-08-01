package com.example.aiy.anime;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

/**
 * <p>功能简述：
 * <p>Created by Aiy on 2017/7/31.
 */

public class PropertyAnimation {
    /**
     * 方法简述： 这是ValueAnimator的简单应用，可以对基本数据动画处理，ofInt、ofFloat没什么难度<br>
     * 参数这里虽然是两个，但是那个方法是不定长数组，可以任意传多少个都行
     */
    public static void ofFloat(float before,float after){
        ValueAnimator va=ValueAnimator.ofFloat(before,after);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current= (float) animation.getAnimatedValue();
                Log.d("Anime", current+ " ");
            }
        });
        //重复
        va.setRepeatCount(6);
        //延迟播放
        //va.setStartDelay(1000);
        va.setDuration(2000);
        va.start();
    }

    /**
     * 方法简述： ObjectAnimator才是重点，他可以修改object的各种数值,先说最基本的四种动画 alpha、rotation、scaleX、scaleY、TranslationY(XZ)<br>如果要设置成员变量的值那么那个类中必须要setter/getter方法，这几个值都是从View类里面找到的，所以可以使用.
     */
    public static void ObjectAnimation(View v){
        //参数 object、成员、不定长数组
        ObjectAnimator oa=ObjectAnimator.ofFloat(v,"alpha",1.0f,0f,0.4f);
        oa.setDuration(3000);
        oa.start();
    }
    /**
     * 方法简述： 之前那些都是单体，组合动画是AnimatorSet，先把几个动画都各自定义好然后调用play()方法传入动画，是个builder类所以可以另外几个方法<br>with()是和paly()同时进行<br> after()是参数里面的这个动画在play()之前！这里注意不是在之后而是after结束后才play<br>before()是跟after相反<br>
     *     还可以设置监听器addListener 可以直接listener几个方法都实现，也可以是AnimatorListenerAdapter.这是一个已经实现几个方法，你可以根据需要去选择的实现而不是全部都要.
     */
    public static void AnimetorSet(View view){
        AnimatorSet set=new AnimatorSet();
        ObjectAnimator oa=ObjectAnimator.ofFloat(view,"alpha",0.5f,1f);
        ObjectAnimator oa1=ObjectAnimator.ofFloat(view,"translationX",-500f,0f);
        ObjectAnimator oa2=ObjectAnimator.ofFloat(view,"rotation",0f,360f);
        set.play(oa).with(oa2).after(oa1);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        set.setDuration(3000);
        set.start();
    }
    //这是ViewPropertyAnimator，这可以简单的实现对View的操作，其实跟ObjectAnimator功能是一样的，不过这里只能对View实现，而且也只能实现补间的四种效果，不过使得操作变得很容易。
    public static void ViewProperty(final View view){
        //注意！ By的方法必须在前面，不然不会出现效果，这里如果把两个方法对调位置的话，会出现只有平移效果而没透明效果
        view.animate()
                .setDuration(3000)
                .alphaBy(0.5f)
                .alpha(0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.animate().alpha(1f);
                    }
                })
               // .translationY(800f)
                ;
    }
}

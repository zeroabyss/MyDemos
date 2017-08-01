package com.example.aiy.anime.Property;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;

/**
 * <p>功能简述：PointEvaluator是为了计算Point的改变量，因为ObjectAnimator是可以任何类型的，所以里面要有个计算器，所以ofFloat也好都是有一个TypeEvaluator，可以实现这个类进行动画.
 * <p>Created by Aiy on 2017/7/31.
 */
public class PointEvaluator implements TypeEvaluator {
    /**
     * 方法简述： 这个fraction跟其他动画的interpolatedTime是一样的 0~1
     * 然后计算之后返回
     */
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point pointStart= (Point) startValue;
        Point pointEnd= (Point) endValue;
        float x=pointStart.getX()+fraction*(pointEnd.getX()-pointStart.getX());
        float y=pointStart.getY()+fraction*(pointEnd.getY()-pointStart.getY());
        Point point=new Point(x,y);
        return point;
    }
    public static void pointAnimetor(){
        ValueAnimator oa=ValueAnimator.ofObject(new PointEvaluator(),new Point(1,1),new Point(2,2));
        oa.setDuration(2000);
        oa.start();
    }
}

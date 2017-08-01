package com.example.aiy.anime.Property;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>功能简述：自定义View 一个半径为50的圆从左上掉落到左下，并且会变化颜色
 * <p>Created by Aiy on 2017/7/31.
 */

public class MyView extends View {
    //注意这里是int而不是Color！这里是颜色值
    private int color;

    public int getColor() {
        return color;
    }

    //setter方法要改变一下，把画笔颜色变化一下不然就没有效果了
    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        invalidate();
    }

    //半径
    public static final float RADIUS=50f;
    //当前的点位置
    private Point currentPoint;
    //画笔
    private Paint paint;
    //构造方法
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //抗锯齿画笔
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        //颜色为蓝色
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //第一次就初始化点和开始动画，否则只执行绘画
        if (currentPoint==null){
            currentPoint=new Point(RADIUS,RADIUS);
            drawCircle(canvas);
            startAnim();
        }else{
            drawCircle(canvas);
        }
    }
    /**
     * 方法简述： 绘制点
     */
    private void drawCircle(Canvas canvas){
        float x=currentPoint.getX();
        float y=currentPoint.getY();
        canvas.drawCircle(x,y,RADIUS,paint);
    }
    /**
     * 方法简述： 开始动画
     */
    private void startAnim(){
        //开始为50，50但是是个圆也相当于从0.0开始了
        Point start=new Point(RADIUS,RADIUS);
        //结束为减去一个圆的坐标，刚好圆是碰到屏幕边缘
        Point end=new Point(getWidth()-start.getX(),getHeight()-start.getY());
        //自定义的TypeEvaluator
        ValueAnimator va=ValueAnimator.ofObject(new PointEvaluator(),start,end);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //每次更新把当前的位置替换
                currentPoint= (Point) animation.getAnimatedValue();
                //执行onDraw
                invalidate();
            }
        });
        /*va.setDuration(5000);
        va.start();*/
        //这里是Object，因为color是我们自己定义的一个成员变量
        final ObjectAnimator colorOa=ObjectAnimator.ofObject(this,"color",new ColorTypeEvaluator(),Color.BLACK,Color.BLUE,Color.WHITE);
       // colorOa.setRepeatCount(5);
        //组合动画把下落动画和颜色变化结合在意
        AnimatorSet set=new AnimatorSet();
        set.play(va).with(colorOa);
        set.setDuration(4000);
        //这里实现重复时候监听，使得颜色从头开始变，不重置的话颜色一直都是末尾变化的样子
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                colorOa.setObjectValues(Color.BLACK,Color.BLUE,Color.WHITE);
            }
        });
        set.setInterpolator(new MyInterpolator());
        set.start();
    }
}

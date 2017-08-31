package com.example.aiy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.util.Log;
import android.view.View;

/**
 *任务描述： Region区域的简单应用.
 *创建时间： 2017/8/28 20:35
 */
public class Canvas_Region extends View{
    private static final String TAG = "Canvas_Region";
    private Context context;
    private Paint paint;
    private Region region;
    /**
     * 方法简述： 构造方法，简单的把region和paint初始化
     */
    public Canvas_Region(Context context) {
        super(context);
        this.context=context;
        paint=new Paint();
        region=new Region();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // regionSetPath(canvas);
        regionOp(canvas, Region.Op.INTERSECT);
    }

    /**
     * 方法简述： 在构造方法中没有指定region的位置，所以设置位置然后画出来
     */
    private void createRegion(Canvas canvas){
        //设置位置
        region.set(20,20,200,500);
        //画出region，但是canvas没有相关的方法
        drawRegion(canvas,region,paint);
    }

    /**
     * 方法简述： 用set方法只能画矩形,setPath可以画Path的路径
     */
    private void regionSetPath(Canvas canvas){
        //设置为描边可以更形象看出drawRegion的过程
        paint.setStyle(Paint.Style.STROKE);
        //置空
        region.setEmpty();
        Path path=new Path();

        //RectF Rect区别是精度，一个是float 一个是int
        RectF rectF=new RectF(20,20,200,500);
        //画椭圆，逆时针
        path.addOval(rectF, Path.Direction.CCW);
        //设置路径path，这里有两个参数一个是path，另一个是region，结果值是取两个面积的交集
        region.setPath(path,new Region(20,20,200,200));
        drawRegion(canvas,region,paint);
    }
    /**
     * 方法简述： 两个region的取交并集
     * @param canvas 画布
     * @param op enum 根据值不同，会产生不同的取值方法具体看region.png
     */
    private void regionOp(Canvas canvas, Region.Op op){
        //创建两个rect 形成一个十字形
        Rect rect1=new Rect(0,100,300,200);
        Rect rect2=new Rect(100,0,200,300);
        paint.setStyle(Paint.Style.STROKE);
        //先画出rect
        canvas.drawRect(rect1,paint);
        canvas.drawRect(rect2,paint);
        //再将他们各自变成Region
        Region region1=new Region(rect1);
        Region region2=new Region(rect2);

        //核心部分就是op方法(这里根据参数而改变)
        region1.op(region2, op);
        Paint paintRegion=new Paint();
        //为了看出实际面积，所以使用fill
        paintRegion.setStyle(Paint.Style.FILL);
        paintRegion.setColor(Color.RED);
        drawRegion(canvas,region1,paintRegion);

    }
    /**
     * 方法简述： 因为canvas没有region的绘制方法，所以我们要自己绘制region，android封装了一个Regioniterator的类，本质上的从上到下绘制出好多个rect，具体可以看运行效果.
     * @param canvas 画布
     * @param rgn 需要绘制的region
     * @param paint 画笔
     */
    private void drawRegion(Canvas canvas,Region rgn,Paint paint){
        //迭代器，参数为要绘制的region
        RegionIterator iterator=new RegionIterator(rgn);
        //这个rect将是一直迭代的rect值
        Rect rect=new Rect();
        while (iterator.next(rect)){
            //从log可以看出执行了N多次而且top值变化比较小 个位数的增加（可以暂时理解为绘制每一行的rect，当然如果是矩形的话只会执行一次）
            Log.d(TAG, "drawRegion: "+rect.left);
            Log.d(TAG, "drawRegion: "+rect.right);
            Log.d(TAG,rect.top+"");
            //每个rect都绘制,然后形成region
            canvas.drawRect(rect,paint);
        }
    }
}

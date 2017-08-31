package com.example.aiy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * <p>功能简述：手势笔迹的简单应用，可以根据手势的痕迹来画线
 * <p>Created by Aiy on 2017/8/28.
 */

public class Gesture_View extends View {
    /**
     * 变量简述： 这个是记录笔迹的路径，通过监听onTouchEvent然后记录路径.
     */
    private Path path;
    /**
     * 变量简述： 画笔
     */
    private Paint paint;
    /**
     * 变量简述： 这是采用另一种方法贝塞尔曲线实现的方法<p></p>
     * 这两个变量是代表前一个点的坐标.
     */
    private float preX,preY;
    /**
     * 方法简述： 构造方法 简单初始化变量
     */
    public Gesture_View(Context context) {
        super(context);
        path=new Path();
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        //paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
    }
    /**
     * 方法简述： onDraw很简单，就是画一下path
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path,paint);
    }

    /**
     * 方法简述： 核心部分监听事件，将事件对应的XY坐标加入路径，然后两点间形成一个线段绘制成Path
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //注释的代码是简单的方法：连接线 每两个点坐标加入Path然后连接成线段，这种方法使得整体不够平滑，因为曲线的时候是各个小线段组成.
                /*path.moveTo(event.getX(),event.getY());*/
                //这个是用贝塞尔曲线,这样使得线段在转弯处是弧形更加平滑好看，但是有个缺点是贝塞尔必须采用中点来当贝塞尔线的点，所以使得实际线段会比较短，因为首尾两个点都少了一半的路径，但是在这里因为是密集的点，所以不是特别明显，可以忽略不计.

                path.moveTo(event.getX(),event.getY());
                //记录点的坐标
                preX=event.getX();
                preY=event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                //直接两点画线就可以了.这里使用了postInvalidate()，他和invalidate的区别在于post是采用handler发送消息然后使得onDraw重制，所以post是可以用于子线程，而invalidate只能在主线程，所以invalidate的速度会比post快.（这里只是使用一下而已，应该使用invalidate）
                /*path.lineTo(event.getX(),event.getY());*/
                invalidate();
                //endX是中点，前个点和当前坐标相加/2
                float endX= (preX+event.getX())/2;
                float endY=(preY+event.getY())/2;
                //画赛贝尔曲线
                path.quadTo(preX,preY,endX,endY);
                //再记录坐标
                preX=event.getX();
                preY=event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                /*path.reset();
                invalidate();
                break;*/
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}

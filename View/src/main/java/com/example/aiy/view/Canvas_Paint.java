package com.example.aiy.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

/**
 * <p>功能简述：简单的使用Canvas和Paint
 * <p>Created by Aiy on 2017/8/17.
 */

public class Canvas_Paint extends View {
    /**
     * 变量简述： 画笔类 canvas是画布，笔在布上绘制
     */
    private Paint paint;
    /**
     * 变量简述： 这是个矩形辅助类，(l,t,r,b)左上右下
     */
    RectF rectF;
    private Context context;
    public Canvas_Paint(Context context) {
        super(context);
        this.context=context;
        paint=new Paint();
        rectF=new RectF(50,300,250,400);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*因为会多次调用onDraw 所以最好不要在该方法内new一个变量
        Lint会报出Avoid object allocations during draw/layout operations
        (preallocate and reuse instead)*/

        canvasPath(canvas);
    }
    private void canvasBase(Canvas canvas){
        //设置颜色
        paint.setColor(Color.RED);
        //抗锯齿
        paint.setAntiAlias(true);
        //画笔宽度，有些图像（圆，矩形，椭圆等）是在style为fill情况下是没有影响的
        paint.setStrokeWidth(10);
        //阴影面积，画图基本没有效果
        paint.setShadowLayer(10,10,10,Color.GREEN);
        //样式，Fill填充，Fill_STROKE填充描线（如果画笔宽度高了就会看出fill之间的效果了）,STROKE描边
        paint.setStyle(Paint.Style.STROKE);

        //设置画布颜色
        canvas.drawRGB(255,255,1);
        //画圆 (原点X，原点Y，半径，画笔)
        canvas.drawCircle(100,100,70,paint);
        //画线（起点X，Y，终点X，Y,画笔）
        canvas.drawLine(200,100,400,100,paint);

        //多个点，每两个是一个点的XY
        float []points={500,100,600,100,
                500,200,600,200,
                500,300,600,300};
        //画多条线，传入一个点的数组，每两个点画一条线（也就是四个值）
        //这个是画线的另外一个，中间两个参数分别为offset 跳过几个值（注意不是点）,count 总共有几个值,所以2,4的意思就是跳过前两个值（就是第一个点）,然后总共使用四个值（600，100，500，200 两个点，所以画出来是一条线）
        //canvas.drawLines(points,2,4,paint);
        canvas.drawLines(points,paint);


        //画点 （X Y 画笔）
        canvas.drawPoint(700,100,paint);

        //画矩形，画圆角矩形（中间两个是X,Y变化，具体修改看效果，不过只要一个值很小基本是没有效果的）
        //canvas.drawRect(rectF,paint);
        canvas.drawRoundRect(rectF,5,30,paint);

        //这是画椭圆，画椭圆有两个方法，一个是直接传矩形四个值，一个先构成RectF，直接传值的需要API21所以还是用RectF的吧.
        RectF rectFOval=new RectF(50,450,250,540);
        //canvas.drawOval(50,450,250,500,paint);
        canvas.drawOval(rectFOval,paint);

        //这是画弧形,中间0，90是度数，X轴正方形是0°，然后逆时针，true表示画出连接角（形成扇形），false就表示只有弧了
        RectF rectFArc=new RectF(300,450,450,540);
        canvas.drawArc(rectFArc,0,90,true,paint);
    }
    /**
     * 方法简述： 绘制Path有关的方法
     */
    private void canvasPath(Canvas canvas){
        //画笔可以重新设置值
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        //Path类就是路径,通过一些曲线构成的一条路径
        Path path=new Path();
        //起点
        path.moveTo(10,10);
        //连接线
        path.lineTo(10,100);
        path.lineTo(300,100);
        path.lineTo(500,100);
        //闭环，就是首尾相连
        path.close();
        canvas.drawPath(path,paint);

        Path CCWRectPath=new Path();
        RectF rectF=new RectF(10,150,200,250);
        //CCW是逆时针 CW是顺时针
        CCWRectPath.addRect(rectF, Path.Direction.CCW);
        canvas.drawPath(CCWRectPath,paint);

        //需要绘制的文字
        String test="大赛i扫贷哦大赛哦那是撒撒旦阿三ask了家啊看到了距离";
        paint.setColor(Color.CYAN);
        paint.setTextSize(35);
        //在路径上面画文字(文本，路径，距离起始距离，切线偏移，画笔),切线偏移的意思是原本文字是写在切线上面的，可以改变值使得它偏移切线位置
        canvas.drawTextOnPath(test,path,100,20,paint);

        //这个可以重置path
        CCWRectPath.reset();
        //同理可以重置rectF
        rectF.setEmpty();
        rectF.set(10,300,200,400);
        //这是圆角,四个角都是一样15，15
        path.addRoundRect(rectF,15,15, Path.Direction.CW);
        //这是另外一个构造方法 需要传入一个八个值的对应四个角
        /*float radius[]={10,10,15,15,15,15,20,20};
        path.addRoundRect(rectF,radius, Path.Direction.CW);*/
        canvas.drawPath(path,paint);

        //同理圆椭圆弧形 都是一样方法Path.addXXX

        //这里例子正面 path可以构成复杂的图案，而且之间并不需要连接，所以如果如果paint都是一样的情况下可以考虑全部都在一个path里面
        Path path1=new Path();
        path1.moveTo(10,500);
        path1.lineTo(50,500);
        path1.addRect(70,405,80,600, Path.Direction.CW);
        canvas.drawPath(path1,paint);
    }
    /**
     * 方法简述： 设置线帽，看stroke_cap.png 笔帽是添加在原本区域上的，所以会使得区域变大.
     * @param cap 线帽的enum
     */
    private void strokeCap(Canvas canvas,Paint.Cap cap){
        paint.setStrokeCap(cap);
        canvas.drawLine(100,200,300,400,paint);
    }
    /**
     * 方法简述： paint另一些应用.
     */
    private void paint_setting(Canvas canvas){
        //设置拐角,效果见path_effect.png 把直线拐角变成圆形拐角
        //参数是CornerPathEffect（float radius）,内接圆所以半径越大的话拐角弧度就越大.
        paint.setPathEffect(new CornerPathEffect(100));

        //虚线的交替数组，单数为实线长度，偶数为虚线长度，然后就这样队列循环.
        float[] intervals={10,20,20,20};
        //开始的偏移量，从偏移量之后开始交替画虚线，所以通过偏移量可以设置动画使得线段可以流动效果
        int phase=20;
        paint.setPathEffect(new DashPathEffect(intervals,phase));
    }

}

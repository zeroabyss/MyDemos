package com.example.aiy.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

/**
 * <p>功能简述：绘制文字相关的paint设置和计算文本所需要面积方法
 * <p>Created by Aiy on 2017/8/28.
 */

public class Canvas_drawText extends View{
    private static final String TAG = "Canvas_drawText";
    /**
     * 变量简述： 下面有个Typeface需要用到Asset所以需要context.
     */
    private Context context;

    private Paint paint;
    /**
     * 方法简述： 构造方法 简单的设置下paint
     */
    public Canvas_drawText(Context context) {
        super(context);
        this.context=context;
        paint=new Paint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textMinRect(canvas,100,200,"dasasddasxz短发第三");
    }

    /**
     * 方法简述： paint与文字相关的一些设置
     */
    public void paintTextSetting(Canvas canvas){
        paint.reset();

        //设置画笔宽度
        paint.setStrokeWidth(5);
        //画笔填充风格
        paint.setStyle(Paint.Style.FILL);
        //抗锯齿
        paint.setAntiAlias(true);
        //设置文本大小
        paint.setTextSize(40);
        //设置文本对齐方式 left则以绘制的点为起点（正常文本）<p>CENTER是为中心点，平均分到两边，所以要确定显示区域，不然可能无法完全显示，而且当文本只有一个字符（比如说“A”，这样的话中心点是在A的中间，所以如果这时候是以X=0开始的文本的话只会显示右一半的A）<p>RIGHT的话文本会显示在点的左边，这种可以用于从屏幕右边开始计算的情况.
        // paint.setTextAlign(Paint.Align.CENTER);
        //是否为粗体
        paint.setFakeBoldText(true);
        //是否有下划线
        paint.setUnderlineText(true);
        //设置字体的水平倾斜度，一般都是-0.25
        paint.setTextSkewX((float)-0.25);
        //删除线
        paint.setStrikeThruText(true);
        //水平拉伸
        paint.setTextScaleX(2);

        //绘制文字中有个构造函数是charSequence,但是这里却不支持Span
        canvas.drawText("sd范德萨范德萨ahuiahi",20,50,paint);

        //Typeface是样式，可以从系统样式获取也可以从文件或者别的typeface copy过来
        Typeface tf=Typeface.create("宋体",Typeface.NORMAL);
        paint.setTypeface(tf);
        AssetManager am=context.getAssets();
        //      Typeface tf1=Typeface.createFromAsset(am,"fonts/jian_luobo.ttf");
    }
    /**
     * 方法简述： 计算文本text所需要的最小面积
     * @param canvas 画布
     * @param baseLineX 基线的x
     * @param baseLineY 基线的Y
     * @param text 要显示的文本
     */
    private void textMinRect(Canvas canvas,int baseLineX,int baseLineY,String text){

        //为了对比,这里把text用不同颜色画出来
        Paint paint1=new Paint();
        paint1.setTextSize(100);
        paint1.setColor(Color.GREEN);
        canvas.drawText(text,baseLineX,baseLineY,paint1);

        //这里再把文字设置一样大，而且是描边，那么就可以是不是计算正确
        paint.setTextSize(100);
        paint.setStyle(Paint.Style.STROKE);

        //可以通过FontMetrics来确定文本的四格线,看图textRegion.png
        /*FontMetrics::ascent;
        FontMetrics::descent;
        FontMetrics::top;
        FontMetrics::bottom;*/
        //这四个变量是分别代表各自距离基线的距离
        Paint.FontMetricsInt metrics=paint.getFontMetricsInt();
        //top是在基线上方所以是负值
        int top=baseLineY+metrics.top;
        int bottom=baseLineY+metrics.bottom;
        //height是计算出
        int height=bottom-top;
        //measureText这个方法可以测量出文字的长度
        int width= (int) paint.measureText(text);
        //绘出这个text的rect,是绘制text需要的面积,但是并不是最小的面积！因为他采用的是top bottom,但是怎么说最小也要ascent,descent会更接近，但是ascent也不行,因为有可能文字都是单格的字母(asuoe之类的),那么以ascent就不正确了.
        Rect rect=new Rect(baseLineX,top,baseLineX+width,bottom);
        canvas.drawRect(rect,paint);

        //这个才是最小面积
        Rect minRect=new Rect();
        //这个方法可以获得text具体的最小面积(文本，开始位置，末尾位置，将结果赋予Rect)
        paint.getTextBounds(text,0,text.length(),minRect);
        //但是这个方法是以(0,0)为基线，所以结果还不是正确的，必须要加上我们设定的基线值
        minRect.top=baseLineY+minRect.top;
        minRect.bottom=baseLineY+minRect.bottom;
        minRect.right=baseLineX+minRect.right;
        minRect.left=baseLineX+minRect.left;
        //现在才是最小面积
        canvas.drawRect(minRect,paint);

        //通过log发现minRect和ascent是不相等的，所以ascent作为最小面积也不对
        //同时minRect不是一个准确值，存在小数点的误差.实际更准确的最小面积值要比现在的大.
        Log.d(TAG, "textMinRect: "+minRect.top);
        Log.d(TAG, "textMinRect: "+(metrics.ascent+baseLineY));
        Log.d(TAG, "textMinRect: "+minRect.bottom);
        Log.d(TAG, "textMinRect: "+(metrics.bottom+baseLineY));
    }
}

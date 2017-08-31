package com.example.aiy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>功能简述：实现存放标签，如果当前行有位置就放在该行，如果空间不够就加入下一行.
 * <p>Created by Aiy on 2017/8/30.
 */

public class Flow_View extends ViewGroup {

    public Flow_View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        //line是指当前行的高度和宽度
        int lineWidth=0;
        int lineHeight=0;
        //这个是viewgroup的宽高
        int layoutWidth=0;
        int layoutHeight=0;
        //遍历子view
        for (int i=0;i<getChildCount();i++){
            View child=getChildAt(i);
            //因为有margin所以使用这个方法
            measureChildWithMargins(child,widthMeasureSpec,0,heightMeasureSpec,0);
            MarginLayoutParams layoutParams= (MarginLayoutParams) child.getLayoutParams();
            //这里记录child所占的总宽高
            int childWidth=child.getMeasuredWidth()+layoutParams.leftMargin+layoutParams.rightMargin;
            int childHeight=child.getMeasuredHeight()+layoutParams.topMargin+layoutParams.bottomMargin;
            //当前行宽度+child的宽度大于总宽度，说明容不下这个child了，新加一行
            if (lineWidth+childWidth > widthSize){
                //如果当前行比记录的最大宽度还大，就赋值给layoutWidth
                layoutWidth=Math.max(lineWidth,layoutWidth);
                //高度是累加的
                layoutHeight+=lineHeight;

                //另开一行，所以当前行应该是当前child的值
                lineWidth=childWidth;
                lineHeight=childHeight;
            }else {
                //这是当前行可以容得下
                //所以放在当前行，所以lineWidth就得加上当前的值
                lineWidth+=childWidth;
                //该行的高度是由最大的控件决定的.
                lineHeight=Math.max(lineHeight,childHeight);
            }
            //因为我们只有当换行的时候才更新布局宽高,但是并没有包括当前行
            //所以最后一个控件时候就必须把当前行数据更行到总布局中.
            if (i==(getChildCount()-1)){
                layoutHeight+=lineHeight;
                layoutWidth=Math.max(layoutWidth,lineWidth);
            }
        }

        setMeasuredDimension((widthMode==MeasureSpec.EXACTLY)?widthSize:layoutWidth,
                (heightMode==MeasureSpec.EXACTLY)?heightSize:layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //因为每个child的top和left都不是固定的,所以我们需要记录他们的值.
        int top=0;
        int left=0;
        //当前行的宽高
        int lineWidth=0;
        int lineHeight=0;

        for (int i=0;i<getChildCount();i++){
            View child=getChildAt(i);
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            //child的总宽高
            int childHeight=lp.topMargin+lp.bottomMargin+child.getMeasuredHeight();
            int childWidth=lp.leftMargin+lp.rightMargin+child.getMeasuredWidth();
            //跟measure时候一样,当前行容不下
            if (childWidth+lineWidth>getMeasuredWidth()){
                //left值当然从0开始
                left=0;
                //高度是前面的累加
                top+=lineHeight;
                //当前行只有这个控件,所以行的宽高当然等于这个控件了
                lineHeight=childHeight;
                lineWidth=childWidth;
            }else{
                //容得下,就把宽度加上去,取最大值的高度
                lineWidth+=childWidth;
                lineHeight=Math.max(lineHeight,childHeight);
            }
            //画布局的四个点
            //left是当前行前面已经使用的距离+这个child所需要的leftMargin
            int leftPoint=left+lp.leftMargin;
            //已使用的高度+topM
            int topPoint=top+lp.topMargin;
            //left坐标+自身宽度
            int rightPoint=leftPoint+child.getMeasuredWidth();
            //top坐标+自身高度
            int bottomPoint=topPoint+child.getMeasuredHeight();
            child.layout(leftPoint,topPoint,rightPoint,bottomPoint);
            //画完之后把值累加给left
            left+=childWidth;
        }
    }
}

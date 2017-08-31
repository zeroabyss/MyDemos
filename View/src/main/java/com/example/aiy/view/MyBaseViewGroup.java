package com.example.aiy.view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>功能简述：简单的自定义viewgroup,实现垂直分布.
 * <p>Created by Aiy on 2017/8/30.
 */

public class MyBaseViewGroup extends ViewGroup {
    //构造方法
    public MyBaseViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //默认情况下是LayoutParams 只能获得layout_width和layout_height等值
    //如果要使用Margin相关的params就要重写以下这三个方法.
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }
    /**
     * 方法简述： 实现重写的第一步是Measure,测量出view的大小，然后传递给onLayout
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //MeasureSpec由前两位的mode值+30位的size值组合成的.
        //UNSPECIFIED(未指定)=0;没有限制大小，一般比较少用到这个
        // EXACTLY(完全)=1;宽高是确定值,比如100dp match_parent
        // AT_MOST(至多)=2;值有上限但是还不确定,wrap_content
        int widthMeasureMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureMode=MeasureSpec.getMode(heightMeasureSpec);
        int widthMeasureSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSize=MeasureSpec.getSize(heightMeasureSpec);

        //这两个变量是指整个ViewGroup的大小，通过计算子view的高累加，宽是取最大值.
        int height=0;
        int width=0;
        //getChildCount()是获得子view的个数
        for (int i=0;i<getChildCount();i++){
            //获得对应的view
            View child=getChildAt(i);
            //测量child，只有测量之后才能使用getMeasuredHeight/width.
            //还有一个方法是measureChildWithMargins,这个方法会考虑上margin的值，所以如果子View有margin的话要使用这个，不然的话父布局可能会允许子View的值超过我们的预想，因为没有计算margin.
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            //获得margin相关的参数，如果没有重写generateLayoutParams的话是不可以的.
            MarginLayoutParams params=(MarginLayoutParams)child.getLayoutParams();
            //h代表该child的总高度，高度当然是控件自身高度+上方margin+下margin
            int h=child.getMeasuredHeight()+params.topMargin+params.bottomMargin;
            //h代表该child的总宽度 同理是自身+左+右
            int w=child.getMeasuredWidth()+params.leftMargin+params.rightMargin;
            //因为我们实现的是控件LinearLayout的Vertical，所以布局的高度是各个child的高度和
            height+=h;
            //宽度则是采用最大控件的宽
            width=Math.max(width,w);
        }

        //这是最后一步，把测量的值设定，传递给onLayout.所以onLayout就可以使用getMeasureHeight
        //这里是判断mode，如果mode是确定值的话，那么就直接采用确定值
        //如果不是确定值(wrap_content)的话，那么就采用我们测量出来的值
        setMeasuredDimension((widthMeasureMode==MeasureSpec.EXACTLY)?widthMeasureSize:width,
                (heightMeasureMode==MeasureSpec.EXACTLY)?heightMeasureSize:height);
    }
    /**
     * 方法简述： 测量之后是layout布局，布局的时候才真正确定child的位置（l,t,r,b）
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //因为每一行的top不确定，所以需要记录当前行的top值
        int top=0;
        int count=getChildCount();
        for (int i=0;i<count;i++){
            View child=getChildAt(i);
            MarginLayoutParams params= (MarginLayoutParams) child.getLayoutParams();
            int childHeight=child.getMeasuredHeight();
            int childWidth=child.getMeasuredWidth();
            //layout方法就是把child确定四个值，在本例子中
            //left:我们是从最左边开始的，所以只要考虑leftMargin就行了.
            //top:我们的局部变量top是记录当前行的高度，所以只要top+topMargin就可以了
            //right:left+child的宽度
            //bottom：top+child的高度
            child.layout(params.leftMargin,top+params.topMargin,childWidth+params.leftMargin,top+childHeight+params.topMargin);
            //因为从顶至下的，所以每行的top位置是前面几个的累加和.每个child的总高度是自身+topMargin+bottom
            top+=(childHeight+params.topMargin+params.bottomMargin);
        }
    }
}

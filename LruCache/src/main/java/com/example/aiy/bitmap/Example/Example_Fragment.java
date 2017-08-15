package com.example.aiy.bitmap.Example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.example.aiy.bitmap.R;

/**
 * <p>功能简述：存放着GridView的fragment
 * <p>Created by Aiy on 2017/8/14.
 */

public class Example_Fragment extends Fragment {
    private static final String TAG = "Example_Fragment";
    /**
     * 变量简述： GridView布局
     */
    private GridView gridView;
    /**
     * 变量简述： 自定义的ArrayAdapter
     */
    private GridViewAdapter adapter;
    /**
     * 变量简述： 这个是每个子项之间的空格大小
     */
    private int spaceSize;
    /**
     * 变量简述： 每个item的宽度
     */
    private int width;

    public static Example_Fragment newInstance(){
        return new Example_Fragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.exmple_fragment,container,false);

        gridView= (GridView) v.findViewById(R.id.grid_view);
        // TODO: 2017/8/14 设置adapter
        adapter=new GridViewAdapter(getActivity(),0,ImageUrl.imageThumbUrls,gridView);
        gridView.setAdapter(adapter);
        spaceSize=getActivity().getResources().getDimensionPixelSize(R.dimen.spaceSize);
        width=getActivity().getResources().getDimensionPixelSize(R.dimen.columnWidthSize);
        //因为view只有到onResume的时候才会绘制完成，所以想要获得view的宽度可以采用这个监听.
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //计算每一行能容下几个item，我们规定的是每个100dp+1dp的空格线，根据屏幕的尺寸会有不同的大小，向下取整Math.floor
                int columnNum= (int) Math.floor(gridView.getWidth()/(spaceSize+width));
                //如果能容下一个以上
                if (columnNum>0){
                    //计算每个的width，因为在xml中我们设定了如果有多余的空间会自动为子项扩大一下面积
                    int columnWidth=gridView.getWidth()/columnNum-spaceSize;
                    // TODO: 2017/8/14 将column设置成item的height
                    Log.d(TAG, columnWidth+"dsa");
                    //其实这里是为了把item高度变成宽度，也就是正方形，默认是个长方形
                    adapter.setItemHeight(columnWidth);
                    //最后移除了监听
                    gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        //不在主界面的时候把缓存写入文件中（这里是guolin说的，会不会产生顿卡有点疑问）
        adapter.flush();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //AsyncTask的还没完成的任务取消了
        adapter.removeAllTask();
    }
}

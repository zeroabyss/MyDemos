package com.example.aiy.anime.Property;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aiy.anime.R;


/**
 *任务描述： 这个活动是自定义View的布局，如果要启动的话在Manifest里面修改启动。
 *创建时间： 2017/8/1 16:39
 */
public class MyViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myview);
    }
}

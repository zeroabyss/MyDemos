package com.example.aiy.bitmap.Example;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aiy.bitmap.R;
/**
 *任务描述： 采用fragment形式，这里是主Activity
 *创建时间： 2017/8/15 14:57
 */
public class Example_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.example_layout);
        if (fragment==null){
            fragment=Example_Fragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.example_layout,fragment)
                    .commit();
        }
    }
}

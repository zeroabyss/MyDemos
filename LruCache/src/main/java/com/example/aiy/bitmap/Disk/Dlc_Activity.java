package com.example.aiy.bitmap.Disk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aiy.bitmap.R;
/**
 *任务描述： 这个是简单的使用DLC，看fragment.
 *创建时间： 2017/8/15 14:41
 */
public class Dlc_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlc_);
        Fragment fragment=new DLC_Fragment();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frameLayout,fragment)
                .commit();
    }
}

package com.example.aiy.view;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 *任务描述： 主界面，将View加入屏幕中.
 *创建时间： 2017/8/28 20:33
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_view);
       /* FrameLayout frameLayout= (FrameLayout) findViewById(R.id.main_layout);
        frameLayout.addView(new Gesture_View(this));*/
    }
}

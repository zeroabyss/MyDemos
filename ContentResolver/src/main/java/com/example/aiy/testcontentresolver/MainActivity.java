package com.example.aiy.testcontentresolver;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragmentAc.OnChangeListener{
    private static final String TAG = "MainActivity";
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        //String s=getIntent().getComponent().getClassName();
        fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.main);
        if (fragment==null){
            fragment=FragmentAc.newInstance();
            //为fragment注册回调，不过这样就显得有点耦合，想把fragment抽象出来
            //却变成向下转型了，可以试试onAttach()的把ati转换成接口
            ((FragmentAc) fragment).setOnChangeListener(this);
            fm.beginTransaction()
                    .add(R.id.main,fragment)
                    .commit();
        }
    }
    /**
     * 方法简述： 该方法是运行配置方法改变时候而已经在manifest里面申明了禁止重创create时候回调的方法<br>
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this, "land", Toast.LENGTH_SHORT).show();
        }else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "por", Toast.LENGTH_SHORT).show();
        }

    }

    //实现回调 这里是替换碎片
    @Override
    public void change() {
        fm.beginTransaction()
                .replace(R.id.main,new AnimeFragment())
                .commit();
    }
}

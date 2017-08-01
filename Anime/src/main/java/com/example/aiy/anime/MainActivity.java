package com.example.aiy.anime;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 任务描述：anim文件夹是补间动画 drawable里面有逐帧 animator是属性动画
 * 创建时间： 2017/7/31 9:29
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private ImageView iv;
    private Button buttton1;

    /**
     * 方法简述： 在onCreate无法获得控件的宽高，所以应该写在这里
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // TweenAnimation.rotate(iv);
        //TweenAnimation.scale(iv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        iv = (ImageView) findViewById(R.id.imageview);
        //iv.setImageResource(R.drawable.ds);
      /*  AnimationDrawable ad= (AnimationDrawable) iv.getDrawable();
        ad.start();*/
        //TweenAnimation.scale(iv);
        // TweenAnimation.alphaAnimationPlay(iv);

    }

    private void initView() {
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);
        buttton1 = (Button) findViewById(R.id.buttton1);
        buttton1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                /*TweenAnimation.rotate(button);
                TweenAnimation.rotate(iv);*/
                // TweenAnimation.animeSet(button);
    /*            iv.setImageResource(R.drawable.z);
                TweenAnimation.scale(iv);*/
/*                MyAnime myAnime=new MyAnime();
                button.startAnimation(myAnime);*/
                // PropertyAnimation.ofFloat(1f,3f);
                Log.d("anime", iv.getAlpha() + " ");
                //PropertyAnimation.ObjectAnimation(iv);
                //PropertyAnimation.AnimetorSet(iv);
                PropertyAnimation.ViewProperty(iv);

                break;
            case R.id.buttton1:
                //这里是使用属性动画的xml文件
                Animator animator= AnimatorInflater.loadAnimator(MainActivity.this,R.animator.anims);
                animator.setTarget(buttton1);
                animator.start();
                break;
        }
    }
}

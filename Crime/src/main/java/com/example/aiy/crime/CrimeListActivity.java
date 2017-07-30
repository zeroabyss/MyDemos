package com.example.aiy.crime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;
import java.util.UUID;

/**
 *任务描述： List界面的活动,这里用到一个知识点refs.xml可以根据屏幕来返回不同的值
 *创建时间： 2017/7/30 19:49
 */

public class CrimeListActivity extends AppCompatActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks{//appCom是工具栏

    protected int getLayoutResID(){
        return R.layout.activity_masterdetail;
    }
    //onCreate是通用写法 就是把id和new fragment类修改。
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        FragmentManager fm=getSupportFragmentManager();//创建Fragment管理器 都是一样的写法
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);

        if(fragment==null){
            fragment=new CrimeListFragment();//fragment的类
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)//把activity给fragment托管
                    .commit();
        }

    }
    //这里是Fragment给Activity的回调，当碎片需要启动活动或者更换碎片的时候应该给活动来执行，虽然碎片也可以执行但是这样耦合度太高了，应该各施其职。
    @Override
    public void onCrimeSelected(Crime crime) {
        if((findViewById(R.id.detail_fragment_container)==null)){
            Intent i=PagerActivity.newIntent(this,crime.getId());
            startActivity(i);
        }else{
            Fragment detail=CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,detail)
                    .commit();
        }
    }
    //这是
    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment=(CrimeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        if (listFragment!=null)
            listFragment.update();

    }

}

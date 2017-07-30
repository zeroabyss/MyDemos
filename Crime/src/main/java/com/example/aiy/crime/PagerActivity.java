package com.example.aiy.crime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 *任务描述： 采用PagerView实现子项
 *创建时间： 2017/7/30 22:03
 */
public class PagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks{
    private static final String EXTRA_CRIME_ID="com.example.aiy.crime.crime_id";

    private ViewPager viewPager;//可滑动的页面
    private List<Crime> crimeList;

    public static Intent newIntent(Context from, UUID id){//别的窗体给pager的传参方法。
        Intent intent=new Intent(from,PagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,id);//传入uuid给EXTRA
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID uuid=(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);//取出uuid

        viewPager=(ViewPager) findViewById(R.id.view_pager);
        crimeList=CrimeLab.get(this).getCrime();

        FragmentManager fragmentManager=getSupportFragmentManager();//获得管理权
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {//使用starPager
            //必须实现的两个方法

            @Override
            public Fragment getItem(int position) {
                Crime crime=crimeList.get(position);
                return CrimeFragment.newInstance(crime.getId());//获得crime然后传到Fragment
            }

            @Override
            public int getCount() {
                return crimeList.size();
            }
        });

        for (int i=0;i<crimeList.size();i++){//找到匹配的uuid 不然无论点到哪个crime都是显示第一个
            if (crimeList.get(i).getId().equals(uuid)){
                viewPager.setCurrentItem(i);//为pager设置位置。
                break;
            }
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}

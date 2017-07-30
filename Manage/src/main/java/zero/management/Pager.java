package zero.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;


import java.util.List;


/**
 *任务描述： 个人信息PagerView的Activity，学生和管理都是用一个Pager，只不过学生设置Pager不能转，管理员就可以切换页面
 *创建时间： 2017/7/30 15:20
 */

public class Pager extends AppCompatActivity {
    private static final String EXTRA_AD_LIST_FRAGMENT_NUM="num";
    private static final String EXTRA_CLIENT_STU_OR_AD="stu_or_ad";
    private List<Person> list;
    private ViewPager viewPager;
    /**
     * 方法简述： 将学号和flag（学生为1，管理员为其他值）
     */
    public static Intent newInstance(Context context, int num,int stu_or_ad){
        Intent i=new Intent(context,Pager.class);
        i.putExtra(EXTRA_AD_LIST_FRAGMENT_NUM,num);
        i.putExtra(EXTRA_CLIENT_STU_OR_AD,stu_or_ad);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_pager);

        int num=(int)getIntent().getSerializableExtra(EXTRA_AD_LIST_FRAGMENT_NUM);
        final int stu_or_ad=(int)getIntent().getSerializableExtra(EXTRA_CLIENT_STU_OR_AD);
        viewPager=(ViewPager)findViewById(R.id.person_pager_view_pager);
        list=Person_Lib.getPersonLib(this).getList();
        //给ViewPager设置是否可以切换
        viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //如果是学生就不能转
                    if(stu_or_ad==1){
                        return false;
                    } else {
                        return true;
                    }
                }
            });
        FragmentManager fm=getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Person person=list.get(position);
                return Pager_Fragment.newInstance(person.getNum(),stu_or_ad);

            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        for (int i=0;i<list.size();i++){
            if(list.get(i).getNum()==num){
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

}

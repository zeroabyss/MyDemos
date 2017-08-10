package com.example.aiy.viewpager.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.aiy.viewpager.R;

import java.util.ArrayList;
import java.util.List;
/**
 *任务描述： 这个是用Fragment作为子项的ViewPager.当然官方也是推荐使用fragment来作为子项最好.要看效果请manifest里面修改启动项
 *创建时间： 2017/8/10 23:36
 */
public class FragmentPagerActivity extends AppCompatActivity implements Fragment3.OnChangeFragment {
    /**
     * 变量简述： 就是ViewPager的子项集合
     */
    private List<Fragment> list;
    private ViewPager fragment_view_pager;
    private static final String TAG = "FragmentPagerActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_pager);
        initView();
        //这里有两种adapter可以使用,第一种是FragmentPagerAdapter另外就是下面这种
        //这两个的区别是前者会保留已加载的全部fragment,而后者只会保留与当前页面相邻的两个fragment(就是他的左右item,包括他自己总共三个,其他的都destroy了).

        fragment_view_pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            /**
             * 方法简述：这里就是加载对应位置的fragmen.值得一提的是FragmentPagerAdapter就算在getItem里面每次new一个fragment的形式,它也不会重复new,因为它如果已经加载过的position就不会再进入getItem这个方法了.
             */
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
            /**
             * 方法简述： 这个方法不是必须要重写的,而是因为要实现动态更换子项,比如移除某个子项,再添加新的子项,顺序按照list的顺序,删除了某个位置,那么之后的全部往前移.
             * @return 这里有两个方法POSITION_UNCHANGED和POSITION_NONE.前者表示item已经加载好了不会再改变,后者表示处于可改变状态.所以我们要改变item的话就必须设置成NONE,默认是UNCHANGED所以默认是没办法改变子项.顺便一提如果改变子项之后会使得全部子项重建一遍,导致很浪费内存,所以要慎用.
             * */
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
            /**
             * 方法简述： 返回item总数
             */
            @Override
            public int getCount() {
                return list.size();
            }
        });
    }

    private void initView() {
        fragment_view_pager = (ViewPager) findViewById(R.id.fragment_view_pager);
        list=new ArrayList<>();
        list.add(new Fragment1());
        list.add(new Fragment3());
        list.add(new Fragment2());
    }

    /**
     * 方法简述： 这个是在Fragment3里面设置的监听接口,因为要通知Activity改变子项fragment
     * 使用解耦的形式.
     */
    @Override
    public void change() {
        Log.d(TAG, "change: ");
        //就是把第一项移除
        list.remove(0);
        //再多加一个
        list.add(new Fragment2());
        //这个方法,网上是说调用notifyData需要remove了之后才能正确显示view不然的话之前的子项不会消失,但是我测试的时候发现子项会自动消失,所以有待进一步考证
//        fragment_view_pager.removeAllViews();


        fragment_view_pager.getAdapter().notifyDataSetChanged();
    }
}

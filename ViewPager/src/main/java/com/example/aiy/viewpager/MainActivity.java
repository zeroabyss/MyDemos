package com.example.aiy.viewpager;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *任务描述： 这是简单的ViewPager使用,使用FragmentPagerActivity的话请修改Manifest
 *创建时间： 2017/8/10 21:49
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /**
     * 变量简述： view列表，这是ViewPager要显示的所有子项，一个View就是一个布局而不是单个控件.
     */
    private List<View> list;
    private ViewPager view_pager;
    /**
     * 变量简述： 这个是跟随pager一同变化的tab具体效果看程序运行
     */
    private PagerTabStrip pager_tab_strip;
    /**
     * 变量简述： 这里其实是个tab,因为PagerTabStrip实在太难看了,只能自己定义一个跟随pager一起变化.
     */
    private ImageView iv;

    /**
     * 变量简述： 这个是偏移量,因为本例子中的iv不是从0,0开始的,而是把屏幕分成几部分,然后iv会在每个部分的居中.offset就是原点距离第一部分居中的距离量.详情看具体运行效果就知道了.
     */
    private int offset;
    
    /**
     * 变量简述： 这个是iv的具体宽度,xml里面是设置了match,但是实际上是一个图片,这个图片会跟随pager而改变位置,所以实际上是drawbale图片的宽度.
     */
    private int ivWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        /*LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater=LayoutInflater.from(this);*/
        //获得LayoutInflater 上面注释掉的是其他两种获得方法效果一样,但是其实源代码都是相同的,都是本质上调用第一种getSystemService.
        LayoutInflater inflater = getLayoutInflater();
        //把三个view都实例化,除了view3有个button,其他都只是改变背景色.
        View view1 = inflater.inflate(R.layout.layout1, null);
        View view2 = inflater.inflate(R.layout.layout2, null);
        View view3 = inflater.inflate(R.layout.layout3, null);
        //所以以后也可以这样动态的找到控件,通过inflater去获取对应的layout,然后去findID
        Button button = (Button) view3.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "ada", Toast.LENGTH_SHORT).show();
            }
        });

        //这个是设置tab的下划线颜色,具体效果看运行
        pager_tab_strip.setTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
        //pager有三页就是这三个view
        list = new ArrayList<>();
        list.add(view1);
        list.add(view2);
        list.add(view3);

        initImageViewPosition();

        /**
         * 变量简述： ViewPager也可以看成一个类似listView一样的控件,所以他也是需要一个adapter
         */
        PagerAdapter pagerAdapter = new PagerAdapter() {
            /**
             * 方法简述： 这个是移除子项item,ViewGroup在这里可以看成list,把对应的view删掉了就可以了.
             * @param container view集合,添加到ViewPager里面的所有view都包含在这里.
             * @param position item的位置,一般都是结合list使用
             * @param object 这里是instantiateItem返回的对象
             */
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }

            /**
             * 方法简述： 这个是初始化的意思,参数的意思跟上面的destroy是一样的.
             * @return 注意这里的返回值,是个Object,他返回的是这个item的Tag,可以随便定义,可以把View返回也行,到时候要通过这个tag来找到这个item.
             */
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                //这里是直接拿position当tag用
                return position;
            }
            /**
             * 方法简述： 返回列表的总个数
             * @return 总个数
             */
            @Override
            public int getCount() {
                return list.size();
            }

            /**
             * 方法简述： 这个如果是PagerTabStrip或者PagerTitleStrip的话就要重写这个方法,两者在xml中都是要写在ViewPager的子布局中.<br>因为是CharSequence所以就可以想到可以使用spannable了,所以这里再顺便就提下Span.
             * @param position 都是跟上面一样的.
             * @return 这个没有限制是string所以可以考虑span
             */
            @Override
            public CharSequence getPageTitle(int position) {
                //如果要经常插入添加当然是要选择sb了
                SpannableStringBuilder sb = new SpannableStringBuilder(" 位置" + position);
                //获得图片,顺便以下方法已经过时了,所以新方法采用ContextCompat的,其他的也都是一样采用CC.
                /*getResources().getDrawable();*/
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_action_name);
                //设置图片位置
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                /*
                 * 变量简述： 这个是在输入框插入图片的时候使用.
                 * 第一个参数是资源Drawable,第二个是对齐方式,有两种第一种是基线对齐还有就是底部对齐<br>基线就是相当于写英文时候的四条线,如果两个控件中有文字就按照文字的位置对齐(layout),具体这里没有具体测试过是什么样的.<br>还有一种就是底部对齐,不难理解.
                 */
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                //前景色,传入参数是Color
                ForegroundColorSpan fore = new ForegroundColorSpan(Color.RED);
                //setSpan的第一个参数是样式比如上面的前景色,也可以是背景色之类的.
                //2.是起始位置,意思就是选择样式的作用范围.
                //3.是结束位置
                //4.是否包括起始位置和结束位置,EX表示不包括,IN表示包括,注意这里对本次改变没有效果,不过你怎么改都是一样的效果IN_EX,所以0,1的作用范围是0(1没包括),所以下面那个从1开始然后到结束是要length而不是length-1(下标是从0开始的)<br>而这个参数的意思是对后来的值有效果,比如最后一句插入1位置,而1位置包括在fore里面所以会有fore的效果.
                //所以在插入一个span时候相邻两个位置比如0,1指定EX_EX的话就没办法再影响其他插入效果
                //0,0 IN_IN可以保证只影响一个位置
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(fore, 1, sb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //这里直接SS就行了不用SSB
                sb.insert(1, new SpannableString("asd"));
                return sb;
            }
            /**
             * 方法简述： 这里是讲如何把View和Tag配对
             * @param view 这个是当前View
             * @param object 这个是tag就是instantiateItem返回的值 本例子中是
             *               position所以就是去list中get就行了.
             */
            @Override
            public boolean isViewFromObject(View view, Object object) {
                //如果相等则说明是同一个view
                return view == list.get(Integer.parseInt(object.toString()));
            }
        };
        //设置adapter
        view_pager.setAdapter(pagerAdapter);

        //这个是页面变化的时候监听
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //one是每个iv之间的距离,因为iv是居中的,one代表前一个居中坐标离下一个居中坐标的量
            private int one=offset*2+ivWidth;
            //这个是为了获得iv的当前坐标 int[0]为x坐标反之为y
            private int[] ivw=new int[2];
            //这个是记录当前position的值,默认启动是显示第一个页面所以默认为0
            private int currentPosition=0;
            /**
             * 方法简述： 这个是滚动时候监听,所以这里设置的效果就是随着页面滚动而滚动.
             * @param position 当前位置,除非完全切换到另一个界面不然还是当前界面
             * @param positionOffset 这个类似于Anime里面的动画 从0~1
             * @param positionOffsetPixels 变化的坐标值
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //既然有了offset这个参数,那么当然是想到动画了
                //这里一定要设置duration为0,不然的话动画不流畅,平移其实很简单
                //就是 position数量*one代表已经偏移了几个了,再加上正在偏移的百分比
                iv.animate()
                        .setDuration(0)
                        .translationX(position*one+one*positionOffset);

                //这里是另外一种方法利用ObjectAnimator来写的,本质上是一样的,获得iv的坐标,然后从iv的坐标到目标值.
/*                iv.getLocationOnScreen(ivw);
                ObjectAnimator oa=ObjectAnimator.ofFloat(iv,"translationX",ivw[0],position*one+one*positionOffset);
                oa.setDuration(0);
                oa.start();*/
            }
            /**
             * 方法简述： 这里实现了跟上面不一样,上面是实时滚动,这里是每切换到一个位置之后才滚动.
             */
            @Override
            public void onPageSelected(int position) {
                //因为每次移动都是固定值,所以只需要知道当前值和目标值就行了
/*
                Animation animation=new TranslateAnimation(currentPosition*one,position*one,0,0);
                animation.setDuration(300);
                animation.setFillAfter(true);
                iv.startAnimation(animation);
                currentPosition=position;
*/
                //最后再把当前值修改下.

            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    /**
     * 方法简述： 这个是把iv的初始位置改变,因为我们是要居中的,而我们在XML中是原点开始的
     * 利用Matrix来修改
     *
     */
    private void initImageViewPosition(){
        //用Bitmap的方法去获取图片宽度
        ivWidth= BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_name).getWidth();
        Log.d(TAG, "initImageViewPosition: "+ivWidth);

        //这个是从屏幕获取长度和宽度的方法
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth=dm.widthPixels;

        Log.d(TAG, "initImageViewPosition: "+screenWidth);

        //先将屏幕分成size份,然后剪去iv的大小就是剩下左空区和右空,各占一半就直接/2.
        //所以就可以得到原点距离第一个居中位置的偏移量
        offset=(screenWidth/list.size()-ivWidth)/2;

        //这个因为是用Matrix来实现平移效果的,所以这个需要设置scaleType是Matrix类型
        //注释掉的原因是因为我在xml里面已经实现了,所以这里可以不用再改
        // iv.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix=new Matrix();
        //(x,y)
        matrix.postTranslate(offset,0);
        iv.setImageMatrix(matrix);
    }
    private void initView() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        pager_tab_strip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        iv = (ImageView) findViewById(R.id.iv);
    }
}

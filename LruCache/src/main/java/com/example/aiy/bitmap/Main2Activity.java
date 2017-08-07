package com.example.aiy.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
/**
 *任务描述： 这是简单的Lrucache应用，以及图片的缩小比例使用.
 *创建时间： 2017/8/7 21:36
 */
public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main2Activity";
    /**
     * 变量简述： 图片的控件
     */
    private ImageView iv;
    /**
     * 变量简述： 这是缓存的Lru
     */
    private LruCache<String, Bitmap> cache;
    private Button bt;

    /**
     * 方法简述： 这个方法我是用来获取iv的宽和高的,从而得知缩小后的比例不是dp值而是px<br>
     * 根据实例,可以得160dpi情况下:1dp=1px<br>
     * 在下面的button点击事件中有获得dpi比例,用的模拟器是480dpi而显示的是3.0也就是说是160的三倍,所以iv是600px,而我提供的图片已经压错成200px了,实际显示在屏幕上会根据转换480dpi:3dp=1px
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, iv.getHeight() + "   " + iv.getWidth());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_main);
        initView();
        //获得当前程序的最大内存
        int memory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d(TAG, memory + "");
        //cache主要是两个 一个maxSize(参数),sizeOf(每个图片的大小)
        //这里设置成最大内存的八分之一,每个图片大小设置成图片的大小
        cache = new LruCache<String, Bitmap>(memory / 8) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //getRowBytes是每行需要的bytes值相当于宽了
                //所以宽*高=总bytes
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
       // iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.s01));
        //开始加载图片
        loadBitmap(R.drawable.s11, iv);


    }
    /**
     * 方法简述： 这是经过缩放之后的bitmap,这里设置成静态是为了让AsyncTask调用,因为他在另外一个类.
     * @param res getResources()
     * @param resId 资源的id
     * @param reqHeight 改变之后的高度大小px
     * @param reqWidth  同上
     */
    public static Bitmap getBitmapDecode(Resources res, int resId, int reqWidth, int reqHeight) {
        //options可以修改Bitmap的一些参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        //当这个值是true的时候,Bitmap并不会开始加载,而是我们可以从options里面获得这个bitmap对象的值(比如大小,宽高)
        options.inJustDecodeBounds = true;
        //已经设置成true了,所以这里只会赋予options一些内容.
        BitmapFactory.decodeResource(res, resId, options);
        //inSampleSize是缩放倍数,如果是2则长和宽都缩小2倍.
        options.inSampleSize = calculate(options, reqWidth, reqHeight);
        Log.d(TAG, "比例" + options.inSampleSize);
        //修改完毕参数后调用false,这时候再调用就可以开始加载了.
        options.inJustDecodeBounds = false;
        //把修改完毕参数后的Bitmap对象返回
        return BitmapFactory.decodeResource(res, resId, options);
    }
    /**
     * 方法简述： 这个是计算inSampleSize的值,参数很明显.options对象还有修改后的宽和高
     */
    private static int calculate(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //从options里提取出来的,这个是实际的宽和高
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        Log.d(TAG, "图片的长宽"+outHeight);
        //设置默认是1也就是没有变化的倍数
        int sampleSize = 1;
        //如果本来的图片比我们需要的还小,那么就没必要在缩小了
        if (reqHeight < outHeight || reqWidth < outWidth) {
            //这是计算倍数,使用Math.round这个是四舍五入的函数.因为sampleSize是int型的
            int heightRatio = Math.round((float) (outHeight) / (float) reqHeight);
            int widthRatio = Math.round((float) outWidth / (float) reqWidth);
            //返回其中一个最小的,因为图片比例都不一样,假如长度特别小,宽度特别大,如果是选长度则倍率就特别大,图片缩放就更小了,可能都不一定看得清,所以要选最小倍率的,哪怕不能完全显示,但是也要尽可能的占满面积.
            sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return sampleSize;
    }
    /**
     * 方法简述： 这个是Lrucache的添加,因为lru里面是封装的,所以可以使用map属性,而我们这里是图片加载,所以泛型就指定为Bitmap
     */
    public void addBitmap(String key, Bitmap bitmap) {
        //虽然map是不能有重复的key,但是如果已存在的话会覆盖key的value值,所以这里要避免
        if (getBitmap(key) == null) {
            cache.put(key, bitmap);
        }
    }
    /**
     * 方法简述： 这个就是取出value值
     */
    public Bitmap getBitmap(String key) {
        return cache.get(key);
    }


    /**
     * 方法简述： 这个方法就是把上面几个方法都应用起来.
     */
    public void loadBitmap(int resId, ImageView iv) {
        String res = String.valueOf(resId);
        //这里是去lrucache里面取bitmap看看是否有缓存,如果有的话直接取出来
        Bitmap bitmap = getBitmap(res);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        } else {
            //这里就是没有缓存,所以就开始异步加载了.
            // TODO: 2017/8/7 后台加载
            //先设置一张待加载的图片
            iv.setImageResource(R.mipmap.ic_launcher);
            //AsyncTask
            Task task = new Task(iv, this);
            task.execute(resId);
        }
    }

    private void initView() {
        iv = (ImageView) findViewById(R.id.iv);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                //这里是验证iv缩放后的图片的大小,因为wrap_content所以图片的大小是根据缩放后的大小决定的,具体的结果已经写在上面了.
                int s=iv.getHeight();
                Log.d(TAG, s+" "+iv.getWidth());
                //获得当前系统的dpi,也可以是density 这个是直接返回与160dpi的差值
                float scale=getResources().getDisplayMetrics().densityDpi;
                Log.d(TAG, "dpi"+scale);
                break;
        }
    }
}

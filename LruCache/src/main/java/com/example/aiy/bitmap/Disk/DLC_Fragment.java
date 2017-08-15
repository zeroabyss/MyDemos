package com.example.aiy.bitmap.Disk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.aiy.bitmap.Disk.Utils.DiskLruCache;
import com.example.aiy.bitmap.Disk.Utils.MD5;
import com.example.aiy.bitmap.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <p>功能简述：这是一个使用DiskLruCache的简单用例
 * <p>Created by Aiy on 2017/8/13.
 */


public class DLC_Fragment extends Fragment {
    /**
     * 变量简述： 图片的下载地址
     */
    private static final String IMAGEURL="http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
    private static final String TAG = "DLC_Fragment";
    /**
     * 变量简述： DiskLruCache的实例，这个不是sdk内置的，需要导入相应的类.
     */
    private DiskLruCache diskLruCache;
    /**
     * 变量简述： 这里尝试使用线程池来弄
     */
    private ExecutorService es= Executors.newCachedThreadPool();
    /**
     * 变量简述： 自定义的handler类
     */
    private Handler handler=new MyHandler(this);
    /**
     * 变量简述： 下载图片的按钮
     */
    private Button download;
    /**
     * 变量简述： 从文件缓存中取出图片并显示图片的按钮
     */
    private Button showImage;
    private ImageView iv;

    /**
     * 方法简述： 自定义的handler类,因为非静态内部类会隐式持有外部类的实例，所以有可能导致OOM，所以定义成静态内部类，之后在onDestroy里面记得把所以的message任务取消了
     */
    private static class MyHandler extends Handler{
        /**
         * 变量简述： 静态内部类外加弱引用
         */
        private WeakReference<Fragment> reference;
        /**
         * 方法简述： 构造方法
         * @param fragment 把外部fragment实例传进来
         */
        private MyHandler(Fragment fragment) {
            reference=new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
           switch (msg.arg1){
               case 1:
                   Log.d(TAG, "handleMessage: ");
                   //把实例向下转型
                   DLC_Fragment dlc_fragment= (DLC_Fragment) reference.get();
                   //因为是弱引用，所以有可能得不到实例，所以需要先判断
                   if (dlc_fragment!=null){
                       //把iv图片更新
                       dlc_fragment.iv.setImageBitmap((Bitmap) msg.obj);
                   }
           }
        }
    }

    /**
     * 方法简述： 这个方法是取得对应的内置缓存地址或者外存地址
     * @param uniqueName 这个是缓存的子目录，可以自定义.
     * @return 返回的是缓存子目录的对应的File类
     */
    public File getCachePath(Context context,String uniqueName){
        //缓存路径
        String cachePath;
        //MEDIA_MOUNTED是储存媒体已经加载并且可以使用，判断外存状态是否是这种状态.
        //isExternalStorageRemovable()外存是否可以卸载，这里是取反
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
            //外存已经加载完成并且不可以卸载那么就取外存
            cachePath=context.getExternalCacheDir().getPath();
        }else{
            //否则还是放在内存
            cachePath=context.getCacheDir().getPath();
        }
        //pathSeparator是文件操作符，相当于路径之间的符号类似于“/”
        return new File(cachePath+File.pathSeparator+uniqueName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dlc_fragment,container,false);
        download= (Button) v.findViewById(R.id.button1);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //因为这是耗时操作所以要用到线程，这里就采用线程池的方法
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            //把下载地址转换成MD5的hexString
                            String key= MD5.urlKeyToMD5(IMAGEURL);
                            //要写入缓存的话是使用editor,传入对应的key（就是文件名）
                            DiskLruCache.Editor editor=diskLruCache.edit(key);
                            if (editor!=null){
                                //传入默认传0就可以了
                                OutputStream out=editor.newOutputStream(0);
                                //网络下载图片 需要用到这个out
                                if (downloadUrl(IMAGEURL,out)){
                                    //下载成功就commit提交到文件
                                    editor.commit();
                                    Log.d(TAG, "run: over");
                                }else {
                                    //否则就清除
                                    editor.abort();
                                }
                            }
                            //必须flush之后才能把文件写到内存里面去
                            diskLruCache.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        showImage= (Button) v.findViewById(R.id.button2);
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        //这里是从文件中取出图片然后显示

                        String key=MD5.urlKeyToMD5(IMAGEURL);
                        try {
                            //写入是Editor 读取是snapshot
                            DiskLruCache.Snapshot snapshot=diskLruCache.get(key);
                            if (snapshot!=null){
                                InputStream is=snapshot.getInputStream(0);
                                Bitmap bitmap= BitmapFactory.decodeStream(is);
                                Message message=handler.obtainMessage();
                                message.arg1=1;
                                message.obj=bitmap;
                                handler.sendMessage(message);
                            }else {
                                Log.d(TAG, "没有找到图片");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        iv= (ImageView) v.findViewById(R.id.iv);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            //获得缓存路径，新建个子目录"Bitmap"
            File cacheDir=getCachePath(getActivity(),"Bitmap");
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            //取得实例是open(缓存路径，应用版本，传1就行，缓存大小)
            diskLruCache=DiskLruCache.open(cacheDir,getVersionCode(getActivity()),1,10*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 方法简述： 从网络下载图片.
     * @param urlString 下载地址.
     * @param outputStream 这里其实是DiskLruCache.Editor new出来的OutputStream，相当于是指定写入文件的路径.
     * @return 是否下载成功.
     */
    public boolean downloadUrl(String urlString, OutputStream outputStream){
        //就是正常的加载
        HttpURLConnection connection=null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream=null;
        try {
            URL url=new URL(urlString);
            connection= (HttpURLConnection) url.openConnection();
            //采用Buffer效率高
            bufferedInputStream=new BufferedInputStream(connection.getInputStream(),8*1024);
            bufferedOutputStream=new BufferedOutputStream(outputStream,8*1024);
            int len;
            byte[] a=new byte[1024];
            while((len=bufferedInputStream.read(a))>0){
                //这里是写到缓存文件里面去了
                bufferedOutputStream.write(a,0,len);
            }
            //下载成功
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭流
            if (connection!=null){
                connection.disconnect();
            }
            try {
                if (bufferedInputStream!=null){
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream!=null){
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //下载失败
        return false;

    }
    /**
     * 方法简述： 清空所有的缓存文件
     */
    public void deleteCache() throws IOException {
        if (diskLruCache!=null){
            diskLruCache.delete();
        }
    }
    /**
     * 方法简述： 获得缓存文件的总大小
     * @return 单位是byte
     */
    public long getCacheSize(){
        return diskLruCache.size();
    }
    /**
     * 方法简述： 删除某个缓存文件
     */
    public void removeKey(String url) throws IOException {
        String key=MD5.urlKeyToMD5(url);
        diskLruCache.remove(key);
    }

    /**
     * 方法简述： 获得该应用的版本号，这里是open获得实例时候用到，如果版本号更新了，那么对应的所有的缓存文件会被删除.
     */
    public int getVersionCode(Context context){
        try {
            //版本号在packageInfo里面
            PackageInfo info=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //默认返回1
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //把handler全部清空
        handler.removeCallbacksAndMessages(null);
        //关闭diskLruCache
        if (diskLruCache!=null){
            try {
                diskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

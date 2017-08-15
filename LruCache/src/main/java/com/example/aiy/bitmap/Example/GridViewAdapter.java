package com.example.aiy.bitmap.Example;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.aiy.bitmap.Disk.Utils.DiskLruCache;
import com.example.aiy.bitmap.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>功能简述：GridView的Adapter
 * <p>Created by Aiy on 2017/8/14.
 */

public class GridViewAdapter  extends ArrayAdapter<String>{
    private static final String TAG = "GridViewAdapter";
    /**
     * 变量简述： LruCache实例,对应<下载地址，bitmap>
     */
    private LruCache<String,Bitmap> lruCache;
    private DiskLruCache diskLruCache;
    private GridView gridView;
    /**
     * 变量简述： 存储AsyncTask的Map，因为多线程会同时访问，所以加个volatile，
     * 一开始是用Set,但是后来因为getView重复调用多次导致AsyncTask次数太多(很多都是重复的加载)，所以采用Map，先判断这个任务是否存在再决定是否加载
     */
    private volatile Map<String,BitmapTask> tasks;
    /**
     * 变量简述： 这个是item的高度，是根据fragment里面的监听来动态改变高度的
     */
    private int height=20;
    public GridViewAdapter(Context context, int textViewResourceId, String[] objects,
                            GridView gridView) {
        super(context, textViewResourceId, objects);
        this.gridView=gridView;
        //取最大内存的1/8来当作内存缓存的大小
        int maxMemory= (int) Runtime.getRuntime().maxMemory();
        int cacheSize=maxMemory/8;
        lruCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
        tasks=new HashMap<>();
        //缓存路径为系统缓存路径的子路径"Photo"
        File cacheDir = getCacheDir(context, "Photo");
        //路径不存在就创建
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        try {
            //获得实例
            diskLruCache=DiskLruCache.open(cacheDir, getApplicationVersionCode(context),1,10*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法简述： 取得缓存路径 详细见Disk包内的
     */
    private File getCacheDir(Context context, String itemPath){
        String path;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
            path=context.getExternalCacheDir().getPath();
        }else{
            path=context.getCacheDir().getPath();
        }
        return new File(path+File.separator+itemPath);
    }
    /**
     * 方法简述：  取得应用的版本号 详细见Disk包内的
     */
    private int getApplicationVersionCode(Context context){
        try {
            PackageInfo info=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 方法简述： 绘制view，注意getView可能会多次重复调用
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v;
        if (convertView!=null){
            v=convertView;
        }else{
            v= LayoutInflater.from(getContext()).inflate(R.layout.exmple_item,null);
        }
        ImageView iv= (ImageView) v.findViewById(R.id.grid_view_item);
        //判断iv的高度是否等于监听获得的height
        if ( iv.getLayoutParams().height!=height){
            iv.getLayoutParams().height=height;
        }
        //设置一个tag，是下载地址，方便到时候AsyncTask直接更新UI，不需要再把iv传入
        iv.setTag(getItem(position));
        //这个是未加载之前的提示图片
        iv.setImageResource(R.drawable.s01);
        //加载图片
        loadBitmap(iv,getItem(position));

        return v;
    }
    /**
     * 方法简述： 加载图片，先从内存内取，然后再从文件中，最后才是网络
     * @param imageUrl 下载地址
     * @param iv 对应的iv
     */
    private void loadBitmap(ImageView iv,String imageUrl){
        //因为getView的重复调用,导致AsyncTask超出指定的128容量任务输，所以报错，所以不重复添加相同的任务，于是先判断是否有这个任务在进行.（这里也不算太好，如果刚好是有重复的图片就会出现加载不了）
        if (tasks.get(imageUrl)!=null){
            return;
        }
        //先从内存取出来
        Bitmap bitmap=getLruCache(imageUrl);
        //没有则开启线程加载
        if (bitmap==null){
            BitmapTask task=new BitmapTask();
            //添加到Map中
            tasks.put(imageUrl,task);
            //采用多线程模式
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,imageUrl);
        }else {
            if (iv!=null){
                //内存中有的话直接拿出来
                iv.setImageBitmap(bitmap);
            }
        }
    }
    /**
     * 方法简述： 添加图片到LruCache
     */
    private void setLruCache(String key,Bitmap bitmap){
        if (getLruCache(key)==null){
            lruCache.put(key,bitmap);
        }
    }
    /**
     * 方法简述： 取出内存缓存的图片
     */
    private Bitmap getLruCache(String key){
        return lruCache.get(key);
    }

    /**
     *任务描述： 先看缓存文件是否有，没有的话就从网络加载
     *创建时间： 2017/8/15 15:25
     */
    private class BitmapTask extends AsyncTask<String,Void,Bitmap>{
        //图片下载地址，因为Post里面要用到
        private String imageUrl;
        @Override
        protected Bitmap doInBackground(String... params) {
            Log.d(TAG, tasks.size()+"");
            //传进来的参数就是地址
            imageUrl=params[0];
            //转换成MD5
            String key=getMD5(imageUrl);
            //snapshot是要读入
            DiskLruCache.Snapshot snapShot;
            FileInputStream fileIs=null;
            //fd的效率会比fileIS之类的高
            FileDescriptor fileDescriptor=null;

            try {
                snapShot=diskLruCache.get(key);
                if (snapShot==null){
                    // TODO: 2017/8/14 网络加载
                    //editor是写入时候用的
                    DiskLruCache.Editor editor=diskLruCache.edit(key);
                    if (editor!=null){
                        OutputStream out=editor.newOutputStream(0);
                        //下载成功就提交，否则就去掉
                        if (downloadUrl(imageUrl,out)){
                            editor.commit();
                        }else{
                            editor.abort();
                        }
                    }
                    //下载时候是把图片写到缓存文件中，所以我们还要再调出来（可以考虑边写入文件时候边加载）
                    snapShot=diskLruCache.get(key);
                }
                //这里很巧妙的不是写成else，因为上面从网络加载最终也是要读，所以相当于（网络下载后读取）+（本地已拥有）一起使用了
                if(snapShot!=null){
                    fileIs= (FileInputStream) snapShot.getInputStream(0);
                    //据说fd效率高所以采用fd
                    fileDescriptor=fileIs.getFD();
                }

                Bitmap bitmap=null;
                if (fileDescriptor!=null){
                    bitmap= BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
                if (bitmap!=null){
                    //顺便加到内存中，因为本次加载的重复使用到的几率相当高，所以加到LruCache是正确的想法
                    setLruCache(imageUrl,bitmap);
                }
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //之前是fd==null&&fileIs!=null,通过log发现并不会走到这，所以改成这样才能正确关闭流
                if (fileDescriptor !=null ){
                    try {
                        Log.d(TAG, "close()");
                        fileIs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //因为已经把tag设置成下载地址了，所以直接找到对应的Tag
            ImageView iv= (ImageView) gridView.findViewWithTag(imageUrl);
            if (bitmap!=null&&iv!=null){
                iv.setImageBitmap(bitmap);
            }
            //加载完了要把task移除了，不然以后再加载就出现问题了
            tasks.remove(imageUrl);
            Log.d(TAG, tasks.size()+"完成");
        }
    }
    /**
     * 方法简述： 下载地址转换成MD5作为缓存文件的名字
     */
    private String getMD5(String key){
        String md5HexString;
        try {
            MessageDigest digest=MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            md5HexString=bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            md5HexString=String.valueOf(key.hashCode());
        }
        return md5HexString;
    }
    /**
     * 方法简述： byte[]转换成HexString
     */
    private String bytesToHexString(byte[] bytes){
        StringBuilder sb=new StringBuilder();
        for (byte a:bytes) {
            String hex=Integer.toHexString(a& 0xff);
            if (hex.length()==1)
                sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * 方法简述： 网络下载图片
     */
    private boolean downloadUrl(String imageUrl, OutputStream out){
        HttpURLConnection con=null;
        BufferedInputStream is=null;
        BufferedOutputStream bo=null;
        try {
            URL url=new URL(imageUrl);
            con= (HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(con.getInputStream());
            bo=new BufferedOutputStream(out,5*1024);
            int b;
            while((b=is.read())!=-1){
                bo.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (con!=null){
                con.disconnect();
            }
           try{
               if (is!=null){
                   is.close();
               }
               if (bo!=null){
                   bo.close();
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
        }
        return false;
    }
    /**
     * 方法简述： 给fragment监听时候改变图片大小使用的
     */
    public void setItemHeight(int height){
        if (height==this.height)
            return;
        this.height=height;
        notifyDataSetChanged();
    }
    /**
     * 方法简述： 将内容写到缓存文件中
     */
    public void flush() {
        if (diskLruCache!=null){
            try {
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 方法简述： 关闭所有的任务
     */
    public void removeAllTask(){
        if (tasks!=null){
            Set<Map.Entry<String,BitmapTask>> set=tasks.entrySet();
            for (Map.Entry<String,BitmapTask> entry:set) {
                //cancel的布尔值 true表示强制停止可能会出现问题，false等他加载完毕
                entry.getValue().cancel(false);
            }
        }
    }
}

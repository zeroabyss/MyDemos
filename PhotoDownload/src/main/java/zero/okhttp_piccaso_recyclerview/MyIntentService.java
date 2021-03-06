package zero.okhttp_piccaso_recyclerview;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 *任务描述： 执行下载任务的后台服务
 *创建时间： 2017/7/30 23:56
 */
public class MyIntentService extends IntentService {
    public static final String PATH="zero.MyIntentService.path";
    public static final String NAME="zero.MyIntentService.name";

    public static void newInstance(Context context,String url,String name){
        Intent i=new Intent(context,MyIntentService.class);
        i.putExtra(PATH,url);
        i.putExtra(NAME,name);
        context.startService(i);
    }
    /**
     * 方法简述： 构造方法，IntentService需要一个调用super（"类名"）的方法
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String path=intent.getStringExtra(PATH);
            final String name=intent.getStringExtra(NAME);
            down(path,name);
        }
    }
    /**
     * 方法简述： 开始下载，这里采用okhttp
     */
    private void down(String url,String name){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        InputStream inputStream = null;
        File file;
        FileOutputStream fileOutputStream = null;
        try {
            Response response = client.newCall(request).execute();

            String sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
            if (response != null) {

                   /* inputStream=response.body().byteStream();
                    file=new File(sdPath+"/"+name);
                Log.e(TAG, file.getPath() );
                    byte[] b=new byte[1024];
                    int len;
                    fileOutputStream=new FileOutputStream(file);
                    while((len=inputStream.read(b))!=-1){
                        fileOutputStream.write(b,0,len);
                    }
                    response.body().close();
                Log.e(TAG, "下载完成");*/
                //  Toast.makeText(MyIntentService.this,"下载完成",Toast.LENGTH_SHORT).show();

                //获得输入流
                inputStream = response.body().byteStream();
                //把输入流转换成bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                file = new File(sdPath + "/" + name);
                fileOutputStream = new FileOutputStream(file);
                //将bitmap写入文件中
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                //扫描sd卡
                insertImageToSystemGallery(MyIntentService.this, file.getPath(), bitmap);
            /*    Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                        @Override
                        public void run() {Toast.makeText(MyIntentService.this, "下载完成", Toast.LENGTH_SHORT).show();}
                    });
            */
                //发送广播给activity弹出Toast：下载完成
                Intent i = new Intent(MyBroadcast.BROADCAST_NAME);
                sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 方法简述： 这个方法是扫描内存卡，如果不扫描的话会发现在picture文件夹中找不到这张图片（重启才行），所以需要发送一个系统广播让其对文件进行加载。
     */
    public static void insertImageToSystemGallery(Context context, String filePath, Bitmap bitmap){
        //bitmap存储SD中
        // MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}

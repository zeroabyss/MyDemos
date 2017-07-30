package zero.downprogress;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class MyService extends Service {
    /**
     * 变量简述： AsyncTask类
     */
    private Download download;
    private DownBinder downBinder=new DownBinder();
    private static final String TAG = "MyService";
    //接口回调
    private Call call=new Call() {
        //更新下载进度条
        @Override
        public void progress(int progress) {
            getManager().notify(1,getNotification1("downing",progress));
        }

        //下载完毕
        @Override
        public void success() {
            download=null;
            Toast.makeText(MyService.this,"下载成功",Toast.LENGTH_SHORT).show();
        }

        //暂停下载
        @Override
        public void pause() {
            download=null;
            Toast.makeText(MyService.this,"暂停下载",Toast.LENGTH_SHORT).show();
        }

        //下载遇到问题
        @Override
        public void failed() {
            download=null;
            Toast.makeText(MyService.this,"下载失败",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return downBinder;
    }

    class DownBinder extends Binder{
        //开始下载
        public void startDownload(String url){
            if (download==null){
                download=new Download(call);
                download.execute(url);
                Log.d(TAG, "startDownload: ");
                //前台服务，int值不能为0
                startForeground(1,getNotification1("DownLoad",0));
            }
        }

        public void pauseDownload(){
            if (download!=null){
                download.Pause();
            }
        }
    }
    private Notification getNotification(String contentText,int progress){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(MyService.this)
                .setContentTitle(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        if (progress>0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }

    private NotificationManager getManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }


    private Notification getNotification1(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress >= 0) {
            // 当progress大于或等于0时才需显示下载进度
            builder.setContentText(progress + "%");
            //参数（最大值，当前进度，是否流动）流动的意思是有动画效果一直滚动（就是一般APP安装的时候的效果）如果设置true的话设置进度条没有用，会一直显示满的进度条而拥有动画，所以可以下载的时候false，安装的时候true
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
}

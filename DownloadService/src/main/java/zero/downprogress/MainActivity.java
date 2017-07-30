package zero.downprogress;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
/**
 *任务描述： 主窗体，拥有两个按钮，一个是开始下载，一个是暂停下载
 *创建时间： 2017/7/29 10:09
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /**
     * 变量简述： 开始下载按钮
     */
    private Button start;
    /**
     * 变量简述： 停止下载
     */
    private Button stop;
    private MyService.DownBinder downBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downBinder= (MyService.DownBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * 方法简述： 绑定了服务要记得解除
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start= (Button) findViewById(R.id.start);
        stop= (Button) findViewById(R.id.stop);
        Intent i=new Intent(this,MyService.class);
        //startService(i);
        bindService(i,connection,BIND_AUTO_CREATE);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downBinder==null){
                    Log.d(TAG, "onClick1: ");
                    return;
                }

                //url是下载的链接
                String url="http://gdown.baidu.com/data/wisegame/b930ea4cd171ad47/baiduditu_811.apk";
                //开始下载的方法
                downBinder.startDownload(url);
                Log.d(TAG, "onClick: ");
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downBinder==null){
                    return;
                }
                //暂停下载
                downBinder.pauseDownload();
            }
        });
    }

}

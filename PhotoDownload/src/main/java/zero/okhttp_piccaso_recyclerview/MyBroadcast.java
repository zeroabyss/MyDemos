package zero.okhttp_piccaso_recyclerview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 *任务描述： 后台下载图片任务完成通知  MyService通知给MainActivity使用
 *创建时间： 2017/7/28 16:22
 */

public class MyBroadcast extends BroadcastReceiver {
    /**
     * 变量简述： intentfilter判断值
     */
    public static final String BROADCAST_NAME="my_intent_service";
    @Override
    public void onReceive(Context context, Intent intent) {
        //因为使用的是自定义action值，为了能够扩展 所以选择了通过action值来判断
        String name=intent.getAction();
        //常量equals变量，如果相反 变量为null会报空指针
        if (BROADCAST_NAME.equals(name)){
            Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
        }
    }
}

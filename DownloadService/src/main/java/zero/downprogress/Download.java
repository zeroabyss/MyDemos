package zero.downprogress;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import static android.content.ContentValues.TAG;
/**
 *任务描述： 下载的核心代码AsyncTask，参数(下载地址，progress进度，返回结果)
 *创建时间： 2017/7/29 10:11
 */

public class Download extends AsyncTask<String,Integer,Integer> {
    /**
     * 变量简述： 作为download的flag，下载，暂停，失败，成功
     */
    private int STATUS=0;
    private final int PAUSE=1;
    private final int FAILED=2;
    private final int SUCCESS=3;
    private Call call;
    /**
     * 变量简述： 最新的progress进度值
     */
    private int lastProgress;
    /**
     * 变量简述： 构造方法：将接口回调传入
     */
    public Download(Call call) {
        this.call=call;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case PAUSE:
                call.pause();
                break;
            case FAILED:
                call.failed();
                break;
            case SUCCESS:
                call.success();
                break;
            default:
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress=values[0];
        //如果进度比之前多就更新progress进度条
        if (progress>lastProgress) {
            //回调给服务，更新进度条
            call.progress(progress);
            //更新最近进度
            lastProgress=progress;
        }
    }

    @Override
    protected Integer doInBackground(String... params) {
        //从服务那获得参数url地址
        String url=params[0];
        InputStream inputStream=null;
        File file=null;
        //RandomAccessFile 可以任意选择写的位置，用到这个是为了暂停之后可以从暂停处继续下载，断续加载
        RandomAccessFile saveFile=null;
        try {
            //下载的长度，可以从文件中读取然后继续写入，也可以判断是否已经下载完成
            long downloadlength=0;
            //获得地址最后“/”切割的那部分，将那部分作为文件名字
            String filename=url.substring(url.lastIndexOf("/"));
            Log.d(TAG, filename);
            //downloads文件夹path
            String directory= Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS).getPath();
            Log.d(TAG, directory);
            //所以地址就是文件放在downloads文件夹中
            file=new File(directory+filename);
            //如果文件已存在，就把那个文件的内容长度给downloadlength
            if (file.exists()){
                downloadlength=file.length();
            }
            //采用OkHttp实现下载功能
            OkHttpClient okHttpClient1=new OkHttpClient();
            Request request1=new Request.Builder()
                    .url(url)
                    .build();
            Response response1=okHttpClient1.newCall(request1).execute();
            //下载文件的总长度
            long contentLength=response1.body().contentLength();
            //关闭的原因是要根据情况重新获得请求
            response1.body().close();
            //总长度是0说明原本地址有问题
            if (contentLength==0){
                return FAILED;
            }else if (downloadlength==contentLength){
                //长度相等，下载完成
                Log.e(TAG, "se");
                return SUCCESS;
            }

            OkHttpClient okHttpClient=new OkHttpClient();
            //这里多一个报头，种类是range 可以从指定的地方开始下载，这里是从已经下载的downloadlength长度开始
            Request request=new Request.Builder()
                    .url(url)
                    .addHeader("RANGE","bytes="+downloadlength+"-")
                    .build();
            Response response=okHttpClient.newCall(request).execute();
            if (response!=null){
                inputStream=response.body().byteStream();
                byte[] b=new byte[1024];
                int len;
                int total=0;
                //开启读写功能
                saveFile=new RandomAccessFile(file,"rw");
                //将文件写的位置指定为最后
                saveFile.seek(downloadlength);
                Log.d(TAG, "doInBackground: ");
                while ((len=inputStream.read(b))!=-1){
                    //每次读的时候先判断是不是用户选择暂停
                    if (STATUS==PAUSE){
                        Log.e(TAG, "PAUSE" );
                        return PAUSE;
                    }
                    total+=len;
                    saveFile.write(b,0,len);
                    //将进度转换成百分比
                    int pr= (int) ((total+downloadlength)*100/contentLength);
                    publishProgress(pr);
                }
                Log.e(TAG, "ok" );
                response.body().close();
                return SUCCESS;
            }
            response.body().close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
           try{
               if (inputStream!=null) {
                   inputStream.close();
               }
               if (saveFile!=null) {
                   saveFile.close();
               }
           }catch (IOException e){
               e.printStackTrace();
           }

        }
        return FAILED;
    }


    public void Pause(){
        STATUS=PAUSE;
    }


}

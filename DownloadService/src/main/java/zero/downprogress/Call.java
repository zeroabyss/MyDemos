package zero.downprogress;

/**
 *任务描述：接口AsyncTask过程回调给service
 *创建时间： 2017/7/29 10:39
 */

public interface Call {
    //更新进度
    void progress(int progress);
    //下载成功
    void success();
    //暂停下载
    void pause();
    //下载失败
    void failed();
}

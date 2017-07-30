package zero.okhttp_piccaso_recyclerview;

/**
 *任务描述： 本接口是“我的收藏”给活动调用的。当“我的收藏”取消了喜欢标志，通知活动同步更新状态
 *创建时间： 2017/7/28 22:19
 */

public interface OnItemListen {
    void change(int position,boolean love,String name);
}

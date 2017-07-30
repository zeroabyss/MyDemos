package zero.okhttp_piccaso_recyclerview.database;

import org.litepal.crud.DataSupport;

/**
 *任务描述： “我的下载”的数据库
 *创建时间： 2017/7/28 22:11
 */

public class My_Down extends DataSupport {
    /**
     * 变量简述： 图片名字
     */
    private String name;
    /**
     * 变量简述： 图片的地址
     */
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

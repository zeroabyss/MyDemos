package zero.okhttp_piccaso_recyclerview.database;

import org.litepal.crud.DataSupport;

/**
 * Created by Aiy on 2017/5/15.
 */

public class My_Like extends DataSupport {
    /**
     * 变量简述： 主键
     */
    private int id;
    /**
     * 变量简述： 图片的名字
     */
    private String name;
    /**
     * 变量简述： 图片的下载地址
     */
    private String url;
    /**
     * 变量简述： 是否加入我的收藏
     */
    private Boolean love;
    /**
     * 变量简述： 在原本list的位置，给回调的时候用，如果在“我的收藏”里面取消了喜欢按钮，这时候主界面的list也跟着变化
     */
    private int listPosition;

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Boolean getLove() {
        return love;
    }

    public void setLove(Boolean love) {
        this.love = love;
    }
}

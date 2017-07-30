package zero.swe;

/**
 * Created by Aiy on 2017/1/22.
 */
/**
 *任务描述： bean
 *创建时间： 2017/7/30 11:04
 */
public class Msg {
    /**
     * 变量简述： flag，信息是接收，说明是别人发过来的信息，显示在左边
     */
    public static final int TYPE_RECEIVED=0;
    /**
     * 变量简述： 信息是发送
     */
    public static final int TYPE_SEND=1;

    private String content;
    private int type;

    public Msg(String content,int type){
        this.type=type;
        this.content=content;
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }
}

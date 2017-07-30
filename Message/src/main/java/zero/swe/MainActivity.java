package zero.swe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
/**
 *任务描述： 主界面的list和初始化信息
 *创建时间： 2017/7/30 10:42
 */
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button send;
    private EditText msgEt;
    private MsgAdapter msgAdapter;
    private List<Msg> msgList= new ArrayList<>();
    /**
     * 变量简述： 初始化信息
     */
    private void initMsgs(){
        Msg msg1=new Msg("hello",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2=new Msg("who?",Msg.TYPE_SEND);
        msgList.add(msg2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initMsgs();
        msgAdapter=new MsgAdapter(MainActivity.this,R.layout.list_item,msgList);

        msgEt= (EditText) findViewById(R.id.input_text);
        send= (Button) findViewById(R.id.send);
        listView= (ListView) findViewById(R.id.msg_list_view);
        listView.setAdapter(msgAdapter);
        //send是发送消息按钮，把edit的文本框的内容发送过去
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg sendText=new Msg(msgEt.getText().toString(),Msg.TYPE_SEND);
                msgList.add(sendText);
                msgAdapter.notifyDataSetChanged();
                //把listView定位在最后,以前是写size()，后来看了源码发现是index是从0开始，所以改成size()-1,
                listView.setSelection(msgList.size()-5);
                msgEt.setText("");
            }
        });

    }
}

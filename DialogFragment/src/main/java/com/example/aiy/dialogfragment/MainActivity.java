package com.example.aiy.dialogfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/**
 *任务描述： 主界面活动，就几个按钮测试功能，这次布局调整有点不对，可以重新弄一下.
 *创建时间： 2017/8/2 18:27
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,AlertDialogFragment.OnFirstItemClickListener {
    private FragmentManager fm = getSupportFragmentManager();
    private Button empty_dialog;
    private Button AlterDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //这里是启动碎片
        if (savedInstanceState==null){
            fm.beginTransaction()
                    .add(R.id.container,new MyFragment())
                    .commit();
        }
    }
    //初始化控件
    private void initView() {
        empty_dialog = (Button) findViewById(R.id.empty_dialog);
        empty_dialog.setOnClickListener(this);
        AlterDialogFragment = (Button) findViewById(R.id.AlertDialogFragment);
        AlterDialogFragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //启动一个没有实际内容的dialog，不过后来我放进了一张图片
            case R.id.empty_dialog:
                EmptyDialogFragment empty = new EmptyDialogFragment();
                empty.show(fm, "empty");
                break;
            //标准的AlertDialogFragment
            case R.id.AlertDialogFragment:
                new AlertDialogFragment().show(fm,"Alter");
                /*AlertDialog ad=new AlertDialog.Builder(this)
                        .setView(R.layout.alert)
                        .create();
                ad.show();*/
                break;
        }
    }

    @Override
    public void click() {
        Toast.makeText(this,"activity",Toast.LENGTH_SHORT).show();
    }
}

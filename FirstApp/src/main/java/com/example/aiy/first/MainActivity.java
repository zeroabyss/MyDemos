package com.example.aiy.first;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 *任务描述： 实现功能是显示问题然后回答问题，check选项可以去另外一个ati查看答案再返回，然后回答问题的时候会显示你作弊了
 *创建时间： 2017/7/30 10:37
 */
public class MainActivity extends AppCompatActivity {
    private Button mZero;//左按钮
    private Button mAbyss;//右
    private TextView mQuestionTextView;//问题窗口
    private Button mcheck;//跳转按钮
    private static final int REQUEST_CODE=0;
    private boolean IsCheater;

    private Question[] mQuestion=new Question[]{//定义问题函数
        new Question(R.string.one,false),
        new Question(R.string.two,false),
        new Question(R.string.three,true),
    };

    private void upDate(){//更新问题
        int question=mQuestion[jishu].getmTextResId();
        mQuestionTextView.setText(question);
    }


    private void check(boolean answer){//根据选项选择对错
        boolean answerIsTrue=mQuestion[jishu].ismAnswerTrue();
        //Toast的文本id
        int id;
        //如果作弊了
        if(IsCheater){
            id=R.string.cheater;
        }else{
            //答案正确
            if(answer==answerIsTrue){
                id=R.string.zeroToast;
            }else{
                id=R.string.abyssToast;
            }
        }


        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
    private int jishu=0;//问题数组的下标

    public void onSaveInstanceState(Bundle savedInstanceState){
        //暂存 用于横转向后保存数据
        super.onSaveInstanceState(savedInstanceState);
        //把jishu给change 存入saved函数中
        savedInstanceState.putInt("change",jishu);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {//创建函数
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionTextView=(TextView)findViewById(R.id.centerText);
        upDate();//更新

        mZero=(Button)findViewById(R.id.zero);
        mAbyss=(Button)findViewById(R.id.abyss);
        mZero.setOnClickListener(new View.OnClickListener(){//匿名内部类 设置点击监听器

            @Override
            public void onClick(View v) {
                check(true);
                jishu=(jishu+1)%mQuestion.length;
                upDate();

            }
        });

        mAbyss.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                check(false);
                jishu=(jishu+1)%mQuestion.length;
                upDate();

            }
        });

        mcheck=(Button)findViewById(R.id.check);
        mcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answer=mQuestion[jishu].ismAnswerTrue();
                Intent i=CheckActivity.newIntent(MainActivity.this,answer);
                startActivityForResult(i,REQUEST_CODE);
            }
        });
        if(savedInstanceState!=null){//用于转向后修改
            jishu=savedInstanceState.getInt("change");//拿出change值
        }
        upDate();//配合saved使用 转向后赋值jishu
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK){
            if(data==null){
                return;
            }
            IsCheater=CheckActivity.getRequest(data);
        }
    }
}

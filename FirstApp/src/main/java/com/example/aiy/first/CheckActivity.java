package com.example.aiy.first;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *任务描述： 可以查看问题的答案
 *创建时间： 2017/7/30 10:39
 */
public class CheckActivity extends AppCompatActivity {
    //来自MainActivity的answertrue，为了命名不重复而且好区分采用包名。
    private static final String EXTRA_ANSWER_IS_TRUE="com.example.aiy.first.MainActivity.answertrue";
    private static final String EXTRA_ANSWER_BUTTON="com.example.aiy.first.IsAnswerButton";

    private boolean AnswerIsTrue;
    private TextView answerTextView;
    private Button  showAnswer;

    public static boolean getRequest(Intent i){
        return i.getBooleanExtra(EXTRA_ANSWER_BUTTON,false);
    }

    public static Intent newIntent(Context fromPackage, boolean answerIsTrue){//来源窗口，来源值
        Intent i=new Intent(fromPackage,CheckActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);//将来源值给对应的EXTRA_ANSWER_IS_TRUE
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);



        AnswerIsTrue=getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        answerTextView=(TextView)findViewById(R.id.answer_Text);
        showAnswer=(Button)findViewById(R.id.show_answer);

        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AnswerIsTrue){
                    answerTextView.setText(R.string.zero);
                }else{
                    answerTextView.setText(R.string.abyss);
                }
                setRequest(true);
            }
        });
    }
    private void setRequest(boolean IsAnswerButton){
        Intent i=new Intent();
        i.putExtra(EXTRA_ANSWER_BUTTON,IsAnswerButton);
        setResult(RESULT_OK,i);
    }

}

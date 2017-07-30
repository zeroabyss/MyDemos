package com.example.aiy.first;

/**
 *任务描述： Bean，问题，成员：问题id，问题答案
 *创建时间： 2017/7/30 10:36
 */
public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int mTextResId,boolean mAnswerTrue){
        this.mAnswerTrue=mAnswerTrue;
        this.mTextResId=mTextResId;
    }
    public int getmTextResId() {
        return mTextResId;
    }

    public void setmTextResId(int mTextResId) {
        this.mTextResId = mTextResId;
    }

    public boolean ismAnswerTrue() {
        return mAnswerTrue;
    }

    public void setmAnswerTrue(boolean mAnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
    }
}

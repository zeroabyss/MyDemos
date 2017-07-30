package zero.management;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

/**
 *任务描述： 个人信息PagerView的Fragment
 *创建时间： 2017/7/30 15:20
 */

public class Pager_Fragment extends Fragment{
    private static final String EXTAR_PAGER_NUM="num";
    private static final String EXTAR_PAGER_STU_OR_AD="stu_or_ad";

    private Person person;
    private EditText name;
    private EditText sex;
    private EditText num;
    private EditText password;
    private EditText from;
    private EditText subject;
    private EditText score;
    /**
     * 变量简述： 这是个flag，如果学生登陆的话则无法编辑个人信息所以把所有的edit都设置成无法编辑状态
     */
    private int isOK;
    /**
     * 变量简述： 这个是当前学号的值，用于如果要修改学号时候，文本已经改变来对比
     */
    private int saved_num;
    public static Pager_Fragment newInstance(int num,int stu_or_ad){
        Bundle bundle=new Bundle();
        bundle.putSerializable(EXTAR_PAGER_NUM,num);
        bundle.putSerializable(EXTAR_PAGER_STU_OR_AD,stu_or_ad);
        Pager_Fragment fragment=new Pager_Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    //EditText的相关事件
    private class Watcher implements TextWatcher{
        private EditText type;
        public Watcher(EditText type){
            this.type=type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        //文本变化之后
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //名字
            if (type==name){
                person.setName(name.getText().toString());
            }
            //性别，先判断是不是正确的性别，以后可以改成选项
            if (type==sex){
                //常量equals变量
                /*s.toString().equals("男")||s.toString().equals("女")||s.toString().equals("")*/
                if("男".equals(s.toString()) ||"女".equals(s.toString())
                        ||"".equals(s.toString())){
                    person.setSex(sex.getText().toString());
                }else{
                    Toast.makeText(getActivity(),"输入错误",Toast.LENGTH_SHORT).show();
                }
            }
            if (type==password){
                person.setPassword(password.getText().toString());
            }
            if (type==from){
                person.setFrom(from.getText().toString());
            }
            if(type==subject){
                person.setSubject(subject.getText().toString());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int num=(int)(getArguments().getSerializable(EXTAR_PAGER_NUM));
        //从学号里面取出这个人信息
        person=Person_Lib.getPersonLib(getActivity()).getPerson(num);
        isOK=(int)getArguments().getSerializable(EXTAR_PAGER_STU_OR_AD);
    }

    private void fullText(EditText et,String s){
        if(! (s==null)){
            et.setText(s);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pager_ex, container, false);

        //设置name选项
        name = (EditText) v.findViewById(R.id.pager_ex_name);
        sex = (EditText) v.findViewById(R.id.pager_ex_sex);
        num = (EditText) v.findViewById(R.id.pager_ex_num);
        password = (EditText) v.findViewById(R.id.pager_ex_password);
        from = (EditText) v.findViewById(R.id.pager_ex_from);
        subject = (EditText) v.findViewById(R.id.pager_ex_subject);
        score = (EditText) v.findViewById(R.id.pager_ex_score);
        //学生的话无法编辑个人信息
        if (isOK == 0) {
            name.setEnabled(false);
            sex.setEnabled(false);
            from.setEnabled(false);
            num.setEnabled(false);
            subject.setEnabled(false);
            score.setEnabled(false);
        }

        fullText(name, person.getName());
        fullText(sex, person.getSex());
        fullText(password, person.getPassword());
        fullText(from, person.getFrom());
        fullText(subject, person.getSubject());
        num.setText(person.getNum()+"");
        score.setText(person.getScore()+"");
        saved_num=Integer.parseInt(num.getText().toString());

        name.addTextChangedListener(new Watcher(name));
        sex.addTextChangedListener(new Watcher(sex));
        password.addTextChangedListener(new Watcher(password));
        from.addTextChangedListener(new Watcher(from));
        subject.addTextChangedListener(new Watcher(subject));
        //num 焦点改变
        num.setOnFocusChangeListener
                (new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //当num失去焦点的时候
                if(!num.hasFocus()){
                    int num1=Integer.parseInt(num.getText().toString());
                    //学号不能为0
                    if (num1==0){
                        Toast.makeText(getActivity(),"学号不为0",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //否则继续去数据库寻找学号一样的学生
                    Person person1=Person_Lib.getPersonLib(getActivity())
                            .getPerson(Integer.parseInt(num.getText().toString()));
                    //如果找到学号一样并且学号和当前本人的值不一样说明是一个新的学号
                    if(person1!=null&&saved_num != num1){
                        Toast.makeText(getActivity(),"学号已存在请更换",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        return  v;
    }

    /**
     * 方法简述： 在暂停的时候执行了一下耗时操作导致变卡，所以这里应该是异步才对
     */
    @Override
    public void onPause() {
        super.onPause();
        person.setNum(Integer.parseInt(num.getText().toString()));
        person.setScore(Integer.parseInt(score.getText().toString()));
        Person_Lib.getPersonLib(getActivity()).updatePerson(person);
    }

}

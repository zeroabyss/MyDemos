package com.example.aiy.crime;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 *任务描述： recycler的子项详情
 *创建时间： 2017/7/30 22:04
 */

public class CrimeFragment extends Fragment{

    private static final String ARG_Crime_Id="Crime_id";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_CONTACT=1;
    private Crime crime;
    private EditText et;
    private CheckBox solved;
    private Button date;
    private Button reportButton;
    private Button suspectButton;
    private Callbacks callbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks=(Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks=null;
    }

    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }
    public static CrimeFragment newInstance(UUID crimeId){//从pager传来uuid
        Bundle args=new Bundle();//新建bundle 之后打包返回list的Fragment
        args.putSerializable(ARG_Crime_Id,crimeId);//uuid 返回

        CrimeFragment fragment=new CrimeFragment();//同时创建自己
        fragment.setArguments(args);//打包
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){//fragment 必须是public 因为activity要调用
        super.onCreate(savedInstanceState);
        //创建时通过uuid给予crime实例
        UUID id=(UUID)getArguments().getSerializable(ARG_Crime_Id);
        crime=CrimeLab.get(getActivity()).getCrime(id);

    }
    //获得要发送的REPORT按钮的文本：文件解决情况（checkbox）+时间+选择联系人
    private String getCrimeReport(){
        String solvedString;
        if (crime.isSolved()){
            solvedString=getString(R.string.crime_report_solved);
        }else{
            solvedString=getString(R.string.crime_report_unsolved);
        }
        String dateFormat="EEE,MMM dd";
        String dateString= DateFormat.format(dateFormat,crime.getDate()).toString();
        String suspect=crime.getSuspect();
        if (suspect==null){
            suspect=getString(R.string.crime_report_no_suspect);
        }else {
            suspect=getString(R.string.crime_report_suspect,suspect);
        }
        String report=getString(R.string.crime_report,crime.getTitle(),dateString,solvedString,suspect);
        return report;
    }


    //onCV主要用途是设置控件的id和值
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime,container,false);


        et=(EditText)v.findViewById(R.id.Crime_Title);
        et.setText(crime.getTitle());
        //editText的监听事件
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
                //文本改变之后更新数据库
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        date=(Button)v.findViewById(R.id.crime_date);
        date.setText(crime.getDate().toString());
        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {//创建选择日期类
                FragmentManager fm=getFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newIntent(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fm,"DIALOG_DATE");
                }});
        //checkbox按钮
        solved=(CheckBox)v.findViewById(R.id.crime_solved);
        solved.setChecked(crime.isSolved());
        //checkbox的点击事件
        solved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    crime.setSolved(isChecked);
                    updateCrime();
                }
            });
        reportButton=(Button)v.findViewById(R.id.crime_report);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送文本到其他APP
                Intent i=new Intent(Intent.ACTION_SEND);
                //能够响应的是支持文本的应用，采用隐式去实现
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                //选择器，用于显示强制弹出可以响应的活动（勾选“以后总是使用”这个功能会失效）
                i=Intent.createChooser(i,getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact=new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        suspectButton=(Button)v.findViewById(R.id.crime_suspect);
        suspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        if (crime.getSuspect()!=null){
            suspectButton.setText(crime.getSuspect());
        }
        //resolveActivity是返回是否有可以响应的活动，如果没有那么不应该启动这个活动，不然就报错
        PackageManager pm=getActivity().getPackageManager();
        if (pm.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY)==null){
            //没有的话就把按钮取消
            suspectButton.setEnabled(false);
        }

        return v;
    }

    //更新数据库,顺便通知主界面更新下数据
    private void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(crime);
        callbacks.onCrimeUpdated(crime);
    }

    //当完毕之后更新数据库
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(crime);
    }

    //从选择日期的DatePickerFragment返回时间值。
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode==REQUEST_DATE){
            //从dialog返回时间
            Date date1=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            crime.setDate(date1);
            updateCrime();
            date.setText(crime.getDate().toString());
        }else if (requestCode==REQUEST_CONTACT){
            //如果是联系人返回一个URI
            Uri contactUri=data.getData();
            //然后提取联系人名字
            String[] queryFields=new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //使用contentResolver来获取联系人信息
            Cursor c=getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);
            try{
                if (c != null && c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String suspect=c.getString(0);
                crime.setSuspect(suspect);
                updateCrime();
                suspectButton.setText(suspect);
            }finally {
                c.close();
            }
        }
    }
}

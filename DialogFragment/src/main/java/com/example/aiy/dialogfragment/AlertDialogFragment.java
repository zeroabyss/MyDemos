package com.example.aiy.dialogfragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * <p>功能简述：这是一个DialogFragment内部封装了一个AlertDialog，现在应该使用DialogFragment来代替Alert，因为DialogFragment有生命周期而且在配置改变如转屏时候会自动处理生命周期,当然转屏的时候也会重建，但是数据却会保留，而Alert并不会而且对话框会消失，所以更应该使用DialogFragment.
 * <p>Created by Aiy on 2017/8/2.
 */

public class AlertDialogFragment extends DialogFragment {
    /**
     * 变量简述： 这是个接口，让活动去执行相应的事件
     */
    private OnFirstItemClickListener listener;
    //构造方法
    public static AlertDialogFragment newInstance(){
        return new AlertDialogFragment();
    }
    //接口拥有一个点击事件，用于view点击
    public interface OnFirstItemClickListener{
        void click();
    }
    //获得listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof OnFirstItemClickListener)
            listener= (OnFirstItemClickListener) context;
    }
    //解除
    @Override
    public void onDetach() {
        super.onDetach();
        if (listener!=null){
            listener=null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //这两种方法都可以
        //LayoutInflater inflater=getActivity().getLayoutInflater();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        //这里设定null,是大部分人都这样用，虽然AS黄色警告.实际上确实不应该这么用比如体现在adapter里面跟根布局的margin属性之类的不会生效(具体目前不大了解)，但是这里是Alert对其影响不大所以这样用可以，可以考虑直接取消警告
        View v=inflater.inflate(R.layout.alert, null);
        //为布局的控件都找到id，然后跟普通的注册没什么区别，只是如果是按钮的话不会点击后取消，如果要关闭对话框的话使用dismiss()
        ImageView iv= (ImageView) v.findViewById(R.id.iv1);
        Button button= (Button) v.findViewById(R.id.button);
        EditText ed= (EditText) v.findViewById(R.id.edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"dasdassa",Toast.LENGTH_SHORT).show();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"IV",Toast.LENGTH_SHORT).show();
                //关闭对话框 这时候这个fragment会执行到destory()就相当于finish()了
                dismiss();
            }
        });
        //正常构建一个ad，注意用setView把布局传入就行了
        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("zs", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.click();
                    }
                });
        return ad.create();
    }
    //以下是调试生命周期用
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("alert", "onCreate: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("alert", "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("alert", "onDestroy: ");
    }
}

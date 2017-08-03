package com.example.aiy.dialogfragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * <p>功能简述：这是一个空的DialogFragment，可以不用实现任何方法，如果提供一个没有子View的布局（设置长宽match），但是显示出来的大小还是默认跟没有实现初始化的一样大，但是如果在里面加入不同的控件的话会显示出来相应的大小，可以占满屏幕.
 * <p>Created by Aiy on 2017/8/2.
 */

public class EmptyDialogFragment extends DialogFragment{

    //这是父类的方法 dialogFragment是extends fragment的，所以也有跟父类一样的生命周期和方法.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.empty_fragment_layout, container, false);
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}

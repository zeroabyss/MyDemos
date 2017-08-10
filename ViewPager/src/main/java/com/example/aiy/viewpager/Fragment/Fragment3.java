package com.example.aiy.viewpager.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.aiy.viewpager.R;

/**
 * <p>功能简述：第三个Fragment
 * <p>Created by Aiy on 2017/8/10.
 */

public class Fragment3 extends Fragment implements View.OnClickListener {

    /**
     * 变量简述： 这个button是给Activity进行回调,让Activity改变list里面的fragment
     */
    private Button button;

    //接口回调
    public interface OnChangeFragment{
        void change();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout3, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        button = (Button) v.findViewById(R.id.button);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                //这里采用的方法是解耦的,如果activity里面实现了这个接口那么就回调,不然的话就没事,所以达到了解耦状态.
                if(getActivity() instanceof OnChangeFragment ){
                  ((OnChangeFragment) getActivity()).change();
                }
                break;
        }
    }
}

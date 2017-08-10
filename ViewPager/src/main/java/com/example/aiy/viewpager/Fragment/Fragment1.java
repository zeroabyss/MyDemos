package com.example.aiy.viewpager.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aiy.viewpager.R;

/**
 * <p>功能简述：第一个Fragment
 * <p>Created by Aiy on 2017/8/10.
 */

public class Fragment1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.layout1,container,false);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fra","destroy");
    }
}

package com.example.aiy.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * <p>功能简述：这里是一个简单fragment，为了从这里启动dialog看一下生命周期，证明生命周期不会像活动启动dialog会pause，这里依然还是处于resume状态，无论是Alert还是dialogFragment.
 * <p>Created by Aiy on 2017/8/2.
 */

public class MyFragment extends Fragment {
    private static final String TAG = "MyFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_fragment,container,false);
        Button button= (Button) view.findViewById(R.id.my_button);
        //简单的按钮启动Alert
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogFragment ad=AlertDialogFragment.newInstance();
                ad.show(getActivity().getSupportFragmentManager(),"fragment");
                /*AlertDialog ad=new AlertDialog.Builder(getActivity())
                        .setView(R.layout.alert)
                        .create();
                ad.show();*/
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}

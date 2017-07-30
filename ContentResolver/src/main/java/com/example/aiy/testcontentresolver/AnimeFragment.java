package com.example.aiy.testcontentresolver;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Aiy on 2017/7/26.
 */

public class AnimeFragment extends Fragment {
    private ImageView imageView;
    private Button button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.anim_fragment,container,false);
        imageView= (ImageView) v.findViewById(R.id.iv);
        button= (Button) v.findViewById(R.id.anime);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.testanim));
                AnimationDrawable animation= (AnimationDrawable) imageView.getDrawable();
                animation.start();
            }
        });
        return v;
    }
}

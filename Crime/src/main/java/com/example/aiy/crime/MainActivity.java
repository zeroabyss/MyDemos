package com.example.aiy.crime;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;
/**
 *任务描述： 这是每个实例的界面Activity
 *创建时间： 2017/7/30 19:48
 */
public class MainActivity extends AppCompatActivity {
    private static final String Extra_Crime_Id="com.example.aiy.crime.id";
    //这里没有调用的原因是因为后来改成用PagerView写了
    public static Intent newIntent(Context from, UUID id){
        Intent intent=new Intent(from,MainActivity.class);
        intent.putExtra(Extra_Crime_Id,id);
        return intent;
    }

 /*  protected Fragment createFragment(){
        UUID crimeId=(UUID)getIntent().getSerializableExtra(Extra_Crime_Id);
        return CrimeFragment.newInstance(crimeId);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);

        if(fragment==null){
            UUID crimeId=(UUID)getIntent().getSerializableExtra(Extra_Crime_Id);
            fragment=CrimeFragment.newInstance(crimeId);

            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }

    }
}

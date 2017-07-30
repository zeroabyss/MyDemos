package com.example.aiy.crime;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 *任务描述： 主界面的Fragment
 *创建时间： 2017/7/30 20:15
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView recyclerView;//显示列表
    private CrimeAdapter adapter;//recyclerView 需要adapter和Viewholder
    private boolean subtitleVisible;//显示工具栏里面的数量，如果true为已显示，false为隐藏
    private static final String SAVED_SUBTITLE_VISIBLE="subtitle";
    private Callbacks callbacks;

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks=null;
    }
    //获得接口实例
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //向下转型
        callbacks=(Callbacks)context;
    }

    //回调给活动让他更换碎片或者启动活动
    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }
    //Fragment的更主要方法是onCreateView而不是onCreate
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //创建视图窗口
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);

        recyclerView=(RecyclerView)view.findViewById(R.id.crime_recycler_view);
        //recyclerView还应该获得LayoutManager的支持
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //这里是暂存subtitleVisible的值，防止转屏时候发现数据丢失。
        if (savedInstanceState!=null){
            subtitleVisible=savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        update();//更新数据（或者初始化数据）
        return view;

    }

    public void update(){//每次更换数据后都需要调该函数
        CrimeLab crimeLab=CrimeLab.get(getActivity());//取出单例
        List<Crime> crimes=crimeLab.getCrime();//从数据库调出List表
        if(adapter==null) {//第一次创建
            adapter = new CrimeAdapter(crimes);//把list给adapter
            recyclerView.setAdapter(adapter);//recyclerView设置adapter，adapter里面会调用viewholder
        }else{
            adapter.setCrimes(crimes);//不是第一次，把更新后的list表给adapter
            adapter.notifyDataSetChanged();//刷新列表
        }
        updateSubTitle();//更新总列表数
    }

    private class CrimeHolder extends RecyclerView.ViewHolder {//viewholder负责个体 adapter负责整体
        private TextView titleView;
        private TextView dateView;
        private CheckBox checkSolved;
        private Crime crime;



        public void bindCrime(Crime crime){//给adapter用
            this.crime=crime;
            titleView.setText(crime.getTitle());
            dateView.setText(crime.getDate().toString());
            checkSolved.setChecked(crime.isSolved());

        }

        public CrimeHolder(View itemView){ //构造函数 两件事1.赋予控件id 2.设置监听事件使得控件可点
            super(itemView);
            titleView=(TextView)itemView.findViewById(R.id.list_item_crime_text_view);
            dateView=(TextView)itemView.findViewById(R.id.list_item_crime_text_view_date);
            checkSolved=(CheckBox)itemView.findViewById(R.id.list_item_crime_solved_checkbox);
            //子项的点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*//pagerActivity的传参数方法（本窗口，传入的值）
                    Intent intent=PagerActivity.newIntent(getActivity(),crime.getId());
                    startActivityForResult(intent,1);//回传*/
                    callbacks.onCrimeSelected(crime);
                }
            });

        }
    }
        private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
            private List<Crime> crimes;

            //每次调完列表会更新 所以要重新调用list表
            public void setCrimes(List<Crime> crimes){
                this.crimes=crimes;
            }
            //初始化时调用list
            public CrimeAdapter(List<Crime> crimes){
                this.crimes=crimes;
            }

            //adapter必须要的三个方法。
            @Override
            public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
                View view=layoutInflater.inflate(R.layout.list_item_crime,parent,false);
                //创建一个视图 然后传给crimeholder操作，由crimeholder获得控件id
                return new CrimeHolder(view);
            }

            //该方法是获取crime例子 然后通过holder的bindCrime方法把所有控件设置crime的值
            @Override
            public void onBindViewHolder(CrimeHolder holder, int position) {
                Crime crime=crimes.get(position);
                holder.bindCrime(crime);
            }

            //获得list的表长。
            @Override
            public int getItemCount() {
                return crimes.size();
            }
    }

    //用于修改完毕pagerActivity后返回时更新列表
    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    //创建工具栏,同时如果控件要修改UI可以在这个方法里面findItem然后去改变
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);//找到工具栏布局文件
        MenuItem subtitleItem=menu.findItem(R.id.menu_item_show_subtitle);//找到显示总数的控件
        if (subtitleVisible){//判断是不是已显示来改变文本
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//允许有工具栏
    }

    //选到工具栏时候，根据获得不同id，找到对应的控件，然后写他们的方法。
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime=new Crime();//新建一个crime
                CrimeLab.get(getActivity()).addCrime(crime);//写入数据库
                /*Intent intent=PagerActivity.newIntent(getActivity(),crime.getId());
                //具体在pager里面修改
                startActivity(intent);//启动*/
                update();
                //通知活动启动Pager
                callbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                subtitleVisible=!subtitleVisible;//点击之后要更换文本
                getActivity().invalidateOptionsMenu();//更新工具栏需要调用此方法
                updateSubTitle();//更新文本
                return true;

            default:
                return super.onOptionsItemSelected(item);//获得父类的所有方法
        }
    }

    private void updateSubTitle(){//副标题
        CrimeLab crimeLab=CrimeLab.get(getActivity());
        int crimesize=crimeLab.getCrime().size();//获得list的总数
        String subtitle=getString(R.string.subtitle_format,crimesize);

        if (!subtitleVisible){//判断是否显示 如果不显示那就设置文本null
            subtitle=null;
        }

        AppCompatActivity activity=(AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);//设置副标题
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {//暂存总数的开关
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,subtitleVisible);
    }
}

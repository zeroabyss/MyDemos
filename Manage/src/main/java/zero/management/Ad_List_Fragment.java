package zero.management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 *任务描述： 管理员主界面的Fragment
 *创建时间： 2017/7/30 13:48
 */

public class Ad_List_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private PersonAdapter adapter;
    private static final int REQUEST=0;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.list_recycler,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        update();
        return view;
    }
    /**
     * 方法简述： 更新信息，如果Adapter是第一次运行则new实例，如果不是则去更新数据
     */
    public void update(){
        Person_Lib person_lib=Person_Lib.getPersonLib(getActivity());
        List<Person> list=person_lib.getList();
        if (adapter==null){
            adapter=new PersonAdapter(list);
            recyclerView.setAdapter(adapter);
        }else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
    }
    }
    /**
     *任务描述： 子项有两个成员变量，一个是名字，一个是学号（其实还有一个照片，但是这里因为方便就设置所有人的照片是统一的，所以就不用列出来）
     *创建时间： 2017/7/30 13:56
     */
    private class PersonHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView num;
        private Person person;
        public PersonHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.list_ex_name);
            num=(TextView)itemView.findViewById(R.id.list_ex_num);
            //item的点击事件，所以item的点击事件是可以设置在Holder的构造方法中
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent=Pager.newInstance(getActivity(),person.getNum(),1);
                    startActivityForResult(intent,1);
                }
            });
        }
        /**
         * 方法简述： onBindViewHolder的内容尽可能的写在viewHolder里面好一点，这样可以解耦，而且如果以后有多个不同的ViewHolder也好扩展。
         */
        public void bingPerson(Person person){
            this.person=person;
            name.setText("姓名："+person.getName());
            num.setText("学号："+person.getNum());
        }
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder>{
        private List<Person> list;
        /**
         * 方法简述： 构造方法 把list传入
         */
        public PersonAdapter(List<Person> persons){
            this.list=persons;
        }

        public void setList(List<Person> persons){
            this.list=persons;
        }
        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            View view=layoutInflater.inflate(R.layout.list_ex,parent,false);
            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position) {
            Person person=list.get(position);
            holder.bingPerson(person);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //拥有菜单
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_ex,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //这是“+”按钮，可以添加一个新的学生
            case R.id.menu_list_ex_new_person:
                Person person=new Person();
                //单例中增加一个学生
                Person_Lib.getPersonLib(getActivity()).addPerson(person);
                //传入的信息为：context，学号（默认为0），如果是学生传入1，其他为管理员
                Intent i=Pager.newInstance(getActivity(),person.getNum(),1);
                startActivity(i);
                return true;
            case R.id.menu_list_ex_delete_person:
                FragmentManager fm=getFragmentManager();
                DeleteFragment delete=new DeleteFragment();
                delete.setTargetFragment(Ad_List_Fragment.this,REQUEST);
                delete.show(fm,"tag");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        //如果从Dialog返回成功的话就刷新一下adapter
        if (requestCode == REQUEST){
            update();
        }
    }
}

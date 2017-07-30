package zero.okhttp_piccaso_recyclerview.Nav;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zero.okhttp_piccaso_recyclerview.Detail_View;
import zero.okhttp_piccaso_recyclerview.R;
import zero.okhttp_piccaso_recyclerview.database.My_Down;
/**
 *任务描述： 该模块是“我的下载”，使用了动态修改recyclerview的布局
 *创建时间： 2017/7/28 22:56
 */
public class Nav_down extends AppCompatActivity {
    private static final String TAG = "Nav_down";
    private RecyclerView re;
    private NavAdapter adapter;
    private List<My_Down> list=new ArrayList<>();
    private Toolbar toolbar;
    public static void newInstance(Context context){
        Intent i=new Intent(context,Nav_down.class);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_down);
        Log.e(TAG, "onCreate: ");
        toolbar= (Toolbar) findViewById(R.id.nav_down_toolbar);
        toolbar.setTitle("我的下载");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        //返回按钮的功能相当于是返回键所以直接调用返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LitePal.getDatabase();
        //把存储我的下载的数据库信息都读出来，并且更新数据库，如果本地没有的图片就从数据库里面删除。
        List<My_Down> allDownUrl=DataSupport.findAll(My_Down.class);
        for (My_Down my_down:allDownUrl){
            Log.e(TAG, my_down.getName() );
            File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+my_down.getName());
            if (file.exists()){
                list.add(my_down);
            }else{
                DataSupport.deleteAll(My_Down.class,"name = ?",my_down.getName());
            }
        }
        //如果没有数据就不用加载了
        if (list.size()>0){
            adapter=new NavAdapter();
            re= (RecyclerView) findViewById(R.id.nav_down_recyclerview);
            re.setAdapter(adapter);
            re.setLayoutManager(new GridLayoutManager(Nav_down.this,3));
        }else {
            Toast.makeText(Nav_down.this,"没有任何下载的文件",Toast.LENGTH_SHORT).show();
        }

    }

    private class NavAdapter extends RecyclerView.Adapter<NavAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v=getLayoutInflater().inflate(R.layout.item,parent,false);
            return  new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //获得recyclerview的子项布局信息
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            //获得imageView的布局信息
            ViewGroup.LayoutParams lp=holder.imageView.getLayoutParams();
            //然后动态改变布局大小，把imageview的大小改成跟子项一样大，占满整个空间
            lp.height=params.height;
            //然后再把布局设置回来
            holder.itemView.setLayoutParams(lp);
            final String sdPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    +"/"+list.get(position).getName();
            File file=new File(sdPath);
            Glide.with(Nav_down.this)
                    .load(file)
                    .placeholder(R.mipmap.down)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=Detail_View.newInstance(Nav_down.this,sdPath,TAG);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public ViewHolder(View itemView) {
                super(itemView);
                imageView= (ImageView) itemView.findViewById(R.id.item);

            }
        }
    }
}

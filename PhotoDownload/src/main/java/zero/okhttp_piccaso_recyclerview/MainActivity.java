package zero.okhttp_piccaso_recyclerview;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import zero.okhttp_piccaso_recyclerview.Nav.Nav_Like;
import zero.okhttp_piccaso_recyclerview.Nav.Nav_down;
import zero.okhttp_piccaso_recyclerview.database.My_Down;
import zero.okhttp_piccaso_recyclerview.database.My_Like;

/**
 *任务描述： 程序的主界面
 *创建时间： 2017/7/30 23:58
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /**
     * 变量简述： 抽屉布局
     */
    private DrawerLayout drawerLayout;
    /**
     * 变量简述： RecyclerView的list
     */
    private List<TestBean.DataBean.WallpaperListInfoBean> list=new ArrayList<>();
    /**
     * 变量简述： 这个是随机高度的list，为了瀑布流
     */
    private List<Integer> heights=new ArrayList<>();
    private RecyclerView re;
    private Myadapter adapter;
    /**
     * 变量简述： 标准的Material design的抽屉布局
     */
    private NavigationView navigationView;
    /**
     * 变量简述： json的地址
     */
    private static  String URL="http://bz.budejie.com/?typeid=2&ver=3.4.3&no_cry=1&client=android&c=wallPaper&a=random&bigid=0";
    /**
     * 变量简述： 刷新
     */
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int REQUESTCODE_PERMISSION=1;
    private FloatingActionButton fab;
    private MyBroadcast myBroadcast;

    /**
     * 方法简述： 返回键，这里添加了一个如果抽屉打开了返回键功能是关闭抽屉
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
       /* if (navigationView.getVisibility()==View.VISIBLE){*/
            drawerLayout.closeDrawers();
        }else
            super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUESTCODE_PERMISSION:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED
                        &&grantResults.length>0){

                }else{
                    Toast.makeText(this,"申请权限失败",Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcast);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //申请权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESTCODE_PERMISSION);
        }
        //初始化布局
        initView();
        //数据库litepal创建数据库
        LitePal.getDatabase();
        //异步获取图片json
        startTask();
        //设置广播接收MyIntentService的回调
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyBroadcast.BROADCAST_NAME);
        myBroadcast=new MyBroadcast();
        registerReceiver(myBroadcast, filter);

        //悬浮按钮实现刷新功能
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               refreshRe();
            }
        });
       // drawerLayout.setFocusableInTouchMode(false);
        //这是其实是toolbar
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            //使Action的home拥有(默认向左小箭头)的图标
            actionBar.setDisplayHomeAsUpEnabled(true);
            //修改图标home
            actionBar.setHomeAsUpIndicator(R.drawable.ic_draweropen);

        }
        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        //左侧栏的三个按钮选项
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_down:
                        //进入"我的下载"
                        Nav_down.newInstance(MainActivity.this);
                        break;
                    case R.id.nav_love:
                        //我的收藏，实现回调功能：同步按钮的图片
                        Nav_Like.newInstance(MainActivity.this, new OnItemListen() {
                            @Override
                            public void change(int position, boolean love,@NonNull String name) {
                                //通过判断
                                if (name.equals(list.get(position).getPicName())) {
                                    Log.e(TAG, position+"" );
                                    Log.e(TAG, name );
                                    list.get(position).setLove(love);
                                    adapter.notifyItemChanged(position);
                                }
                            }
                        });
                        break;
                    default:

                }
                return true;
            }
        });
        //设置刷新图标的中间那个“循环箭头”的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        //刷新的功能
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               refreshRe();
            }
        });
    }
    /**
     * 方法简述： 为控件findViewById
     */
    private void initView() {
        re = (RecyclerView) findViewById(R.id.re);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        re.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer);
        fab= (FloatingActionButton) findViewById(R.id.floatAction);
        setAdapter();
    }
    /**
     * 方法简述： 为recyclerView设置Adapter,其实可以直接放在初始化里面的，但是之前不在初始化的原因，是一开始没有数据，所以把第一次成功返回数据的时候再绑定但是后来发现这是个bug，如果一开始没有网络的话之后就没办法绑定了
     */
    private void setAdapter(){
        adapter=new Myadapter();
        re.setAdapter(adapter);
    }

    private void initHeights(){
        Random random = new Random();

        for (int i = 0; i < list.size(); i++) {

            //集合中存储每个回调图片对应的随机高度
            heights.add(random.nextInt(200)+200);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nulltoolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:

        }
        return true;
    }
    /**
     * 方法简述： 第一次获取json
     */
    private void startTask(){
        OkHttpUtils.getInstance().getBeanOfOk(this, URL, TestBean.class, new OkHttpUtils.CallBack<TestBean>() {
            @Override
            public void getData(TestBean testBean) {
                Log.e("sda",testBean.toString());
                if (testBean.getData()!=null){
                    list.addAll(testBean.getData().getWallpaperListInfo());
                    Log.e(TAG, list.get(0).getID()+"" );
                    initHeights();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.item,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.like = list.get(position).getLove();
            //这里两个按钮设置可见是因为采用了布局复用，因为和我的下载布局相似，而它的布局是没有按钮的
            holder.button.setVisibility(View.VISIBLE);
            holder.likeButton.setVisibility(View.VISIBLE);
            //根据是否喜欢而改变按钮形状
            if (holder.like) {
                holder.likeButton.setImageResource(R.mipmap.like);
            } else {
                holder.likeButton.setImageResource(R.mipmap.unlike);
            }
            //加载图片
            Glide.with(MainActivity.this)
                    .load(list.get(position).getWallPaperMiddle())
                    .placeholder(R.mipmap.down)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imageView);
        /*                Glide.with(MainActivity.this)
                                .load(list.get(position).getWallPaperMiddle())
                                .thumbnail(Glide.with(MainActivity.this).load(R.mipmap.timg))
                                .fitCenter()
                                .crossFade()
                                .into(holder.imageView);*/
            //点击图片会放大
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String photoUrl=list.get(position).getWallPaperDownloadPath();
                    Intent i=Detail_View.newInstance(MainActivity.this,photoUrl,"MainActivity");
                    startActivity(i);
                }
            });
            //下载按钮
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path=list.get(position).getWallPaperDownloadPath();
                    String name=list.get(position).getPicName();
                    //把图片放在pictures文件夹
                    File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+name);
                    //如果之前下载过了就不用再下载
                    if (file.exists()){
                        Toast.makeText(MainActivity.this,"文件已存在",Toast.LENGTH_SHORT).show();
                    }else {
                        //把图片存到数据库中
                        My_Down my_down = new My_Down();
                        my_down.setName(list.get(position).getPicName());
                        my_down.setUrl(list.get(position).getWallPaperDownloadPath());
                        Log.e(TAG, list.get(position).getPicName());
                        Log.e(TAG, list.get(position).getWallPaperDownloadPath());
                        //litepal的保存方法
                        my_down.save();
                        //启动服务去下载图片
                        MyIntentService.newInstance(MainActivity.this, path, name);
                    }
                }
            });

            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //like是有收藏
                    if (holder.like) {
                        holder.likeButton.setImageResource(R.mipmap.unlike);
                        holder.like=false;
                        My_Like my_like=new My_Like();
                        my_like.setToDefault("love");
                        my_like.setListPosition(position);
                        my_like.updateAll("name = ? ",list.get(position).getPicName());
                        Log.e(TAG, position + "   " + list.get(position).getPicName() );
                    }
                    //没有收藏
                    else {
                        //看看数据库里面有没有
                        holder.likeButton.setImageResource(R.mipmap.like);
                        holder.like=true;
                        List<My_Like> likes=DataSupport.where("name = ?",list.get(position).getPicName()).find(My_Like.class);
                        //在数据库里面没有这个。
                        if (likes.size()==0){
                            My_Like my_like = new My_Like();
                            my_like.setName(list.get(position).getPicName());
                            my_like.setUrl(list.get(position).getWallPaperDownloadPath());
                            my_like.setListPosition(position);
                            my_like.setLove(true);
                            my_like.save();
                            Log.e(TAG, position + "   " + list.get(position).getPicName() );
                        }else if(likes.size()==1) {
                            //数据库里有就把在当前的位置修改下，因为到时候我的收藏里面取消了的话，回调给活动顺便取消了
                            My_Like my_like=new My_Like();
                            my_like.setListPosition(position);
                            my_like.setLove(true);
                            my_like.updateAll("name = ? ",list.get(position).getPicName());
                            Log.e(TAG, position + "   " + list.get(position).getPicName()  );
                        }else {
                            Log.e(TAG, "出现意外错误" );
                        }
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            /**
             * 变量简述： 卡片布局
             */
            CardView cardView;
            /**
             * 变量简述： 图片布局
             */
            ImageView imageView;
            /**
             * 变量简述： 下载按钮
             */
            Button button;
            /**
             * 变量简述： 喜欢按钮
             */
            ImageButton likeButton;
            /**
             * 变量简述： 是否喜欢，根据值：红心空心
             */
            Boolean like;

            private ViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView;
                imageView = (ImageView) itemView.findViewById(R.id.item);
                // a= (TextView) itemView.findViewById(R.id.tv);
                button = (Button) itemView.findViewById(R.id.down);
                likeButton = (ImageButton) itemView.findViewById(R.id.like);
            }
        }

    }
    /**
     * 变量简述： 刷新内容，将图片全部换了
     */
    private void refreshRe(){
        OkHttpUtils.getInstance().getBeanOfOk(MainActivity.this, URL, TestBean.class
                , new OkHttpUtils.CallBack<TestBean>() {
                    @Override
                    public void getData(TestBean testBean) {
                        if (testBean.getData()!=null){
                            //recycler返回第一条
                            re.smoothScrollToPosition(0);
                            //因为要换全部内容 所以把list清了
                            list.clear();
                            list.addAll(testBean.getData().getWallpaperListInfo());
                            Toast.makeText(MainActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            //如果不调用setRefreshing方法的话，刷新图标不会关闭而是会一直不停的旋转
                            swipeRefreshLayout.setRefreshing(false);
                          //toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        }else {
                            Toast.makeText(MainActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


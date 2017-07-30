package com.example.aiy.testcontentresolver;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.unit.RequestPermission;

import static android.app.Activity.RESULT_OK;



/**
 *任务描述： 1.查询手机联系人 2.测试documentProvider 3.fragment和activity的回调
 *修改时间： 2017/7/27 23:28
 */
public class FragmentAc extends Fragment {
    private static final String TAG = "FragmentAc";
    /**
     * 变量简述： 对应按钮:test,实现申请权限和获取手机联系人输出到logcat
     */
    private Button button;
    /**
     * 变量简述： 打开documentProvider
     */
    private Button document;
    /**
     * 变量简述： 回调给activity
     */
    private Button change;
    /**
     * 变量简述： 接口 通过活动实现该接口 并且调用setOnChangeListener实现回调
     */
    private OnChangeListener onChangeListener;

    /**
     * 方法简述： 空的构造方法
     */
    public FragmentAc(){}

    public static FragmentAc newInstance(){
        return new FragmentAc();
    }


    public interface OnChangeListener{
        void change();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener){
        this.onChangeListener=onChangeListener;
    }
    //documentprovider的请求码，用于获取返回的文件的相关信息
    public static final int DOCMENT_REQUEST_CODE=1;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: " );
        View v=inflater.inflate(R.layout.list_fragment,container,false);
        button= (Button) v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  lianxi();
                //这里使用是依赖module的unit的获取权限方法
                RequestPermission.checkPermission(FragmentAc.this,Manifest.permission.READ_CONTACTS,REQUEST_CODE);
            }
        });
        document = (Button) v.findViewById(R.id.document);
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(19)
            public void onClick(View v) {
                // TODO: 2017/7/25  返回documentUI列表
                //因为document是需要api19的，传入flag，category是固定的
                //setType可以过滤mime
                if(Build.VERSION.SDK_INT>=19) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent,DOCMENT_REQUEST_CODE);
                }
            }
        });
        change= (Button) v.findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeListener.change();
            }
        });


        return v;
    }
    /**
     * 变量简述： 这是请求权限的请求码
     */
    private static final int REQUEST_CODE=1;

    /**
     * 方法简述： 该方法是结合请求权限之后通过返回值判断是否进行下一步获取联系人
     */
    public void lianxi(){
        if (!checkPermission()){return;}
        ContentResolver cr=getActivity().getContentResolver();

        //这是一种方法
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection=new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        //这是另外一种方法，通过找到id然后去找对应的信息
        Uri u=ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor=cr.query(u, null,null,null,null);
        if (cursor!=null){
            while(cursor.moveToNext()){
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                Log.d(TAG, name);
                String numberId=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor c=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"= ?",new String[]{numberId},null);
                while(c != null && c.moveToNext()){
                    String number=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.d(TAG, number+"");
                }

            }
        }
    }
    public boolean checkPermission(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
            /*AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
                    .setTitle("我们需要权限")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CODE);
                        }
                    });
            builder.show();*/
            Snackbar.make(button,"我们需要权限",Snackbar.LENGTH_INDEFINITE)
                    .setAction("同意", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
                        }
                    }).show();
        }else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    // TODO: 2017/7/24 执行操作
                    lianxi();
                }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    // The document selected by the user won't be returned in the intent.
    // Instead, a URI to that document will be contained in the return intent
    // provided to this method as a parameter.
    // Pull that URI using resultData.getData().
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case DOCMENT_REQUEST_CODE:
                if (resultCode==RESULT_OK){
                    //documentProvider是返回一个uri
                    Uri uri=data.getData();
                   // Log.d(TAG, uri.toString());
                    requestImageData(uri);
                }
        }
    }

    public void requestImageData(Uri uri){
        Cursor cursor=getActivity().getContentResolver().query(uri,null,null,null,null,null);
        try{
            if (cursor!=null&&cursor.moveToFirst()){
                String name=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                //如果size是未知的，那他的返回值是null
                //所以getString为null是没有意义的（如果是其他就不一样了）
                //比如说是int，java的int不能为null，如果我们其他情况要getInt那就报错
                //而且这种情况可能是远程文件，那么就有可能出现特殊情况size是未知
                int sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE);
                String size;
                if (!cursor.isNull(sizeIndex)){
                     size=cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE));
                }else{
                    size="unknown";
                }
                Log.d(TAG, name+"Size "+size);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
    }
}

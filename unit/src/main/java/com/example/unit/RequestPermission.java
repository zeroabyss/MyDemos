package com.example.unit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

/**
 * <p>功能简述：申请权限(Fragment)
 * <p>Created by Aiy on 2017/7/27.
 */

public class RequestPermission {
    /**
     * 方法简述： fragment申请权限<br>
     * @param fragment fragment本体传入，因为需要用到fragment的requestPermissions<br>
     * @param permission 对应manifest里的权限<br>
     * @param requestCode 请求code
     * @return 返回true表示已经拥有此权限，false表示没有并且申请
     */
    public static boolean checkPermission(Fragment fragment, String permission,int requestCode){
        //6.0之前的版本不需要申请权限
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return true;
        if (ContextCompat.checkSelfPermission(fragment.getActivity(),permission)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        //如果之前被拒绝了会调用这个方法
        if(fragment.shouldShowRequestPermissionRationale(permission)){
            Toast.makeText(fragment.getActivity(), "我们需要权限", Toast.LENGTH_SHORT).show();
            fragment.requestPermissions(new String[]{permission},requestCode);
        }else {
            fragment.requestPermissions(new String[]{permission},requestCode);
        }
        return false;
    }

    /**
     * 方法简述: 与fragment的方法一致
     */
    public static boolean checkPermission(Activity activity, String permission,int requestCode){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return true;
        if (ContextCompat.checkSelfPermission(activity,permission)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        if(activity.shouldShowRequestPermissionRationale(permission)){
            Toast.makeText(activity, "我们需要权限", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
        }else {
            ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
        }
        return false;
    }
}

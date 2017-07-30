package zero.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 *任务描述： 管理员的主界面activity
 *创建时间： 2017/7/30 13:48
 */

public class Ad_List extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deep_layout);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.deep_activity);

        if (fragment==null){
            fragment=new Ad_List_Fragment();
            fm.beginTransaction()
                    .add(R.id.deep_activity,fragment)
                    .commit();
        }
    }
}

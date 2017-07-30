package zero.management;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Created by Aiy on 2016/12/11.
 */

public class DeleteFragment extends DialogFragment {
    private EditText et;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.delete,null);
        et=(EditText)v.findViewById(R.id.delete_edit_text);
        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("请输入要删除的学号")
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s=et.getText().toString();
                        //注意这里如果出现了比int的MAX还大就报错
                        Person_Lib.getPersonLib(getActivity()).Delete(Integer.parseInt(s));
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }
    /**
     * 方法简述： 发送回去给管理员界面的fragment
     */
    private void sendResult(int resultCode){
        if (getTargetFragment()==null){
            return;
        }
        Intent i=new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
    }

}

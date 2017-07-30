package zero.swe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 *任务描述： 这是采用listView的arrayAdapter实现的。
 *创建时间： 2017/7/30 11:20
 */

public class MsgAdapter extends ArrayAdapter<Msg>{

    private int resourceId;

    public MsgAdapter(Context context, int resourceId, List<Msg> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg=getItem(position);
        View view;
        ViewHolder viewHolder;
        //convertView是缓存的布局，这也是防止内存不合理使用的一种
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.left= (LinearLayout) view.findViewById(R.id.left_layout);
            viewHolder.right= (LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.TV_Left= (TextView) view.findViewById(R.id.left_msg);
            viewHolder.TV_right= (TextView) view.findViewById(R.id.msg_right);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        assert msg != null;
        if(Msg.TYPE_RECEIVED==msg.getType()){
            viewHolder.left.setVisibility(View.VISIBLE);
            viewHolder.right.setVisibility(View.GONE);
            viewHolder.TV_Left.setText(msg.getContent());
        }else if(msg.getType()==Msg.TYPE_SEND){
            viewHolder.left.setVisibility(View.GONE);
            viewHolder.right.setVisibility(View.VISIBLE);
            viewHolder.TV_right.setText(msg.getContent());
        }
        return view;
    }

    private class ViewHolder{
        LinearLayout left;
        LinearLayout right;
        TextView  TV_Left;
        TextView TV_right;
    }
}

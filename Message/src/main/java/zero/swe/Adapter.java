package zero.swe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
/**
 *任务描述： 这是采用的recyclerView实现本功能
 *创建时间： 2017/7/30 11:06
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private List<Msg> list;
    public Adapter(List<Msg>  list){
        this.list=list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //这里比较简单采用Tv来当作对话框
        TextView left;
        //这是右边就是个人发送的
        TextView right;

        public ViewHolder(View itemView) {
            super(itemView);
            left= (TextView) itemView.findViewById(R.id.left_view);
            right= (TextView) itemView.findViewById(R.id.right_view);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(v);
    }
    /**
     * 方法简述： 该方法可以返回一个子项的类型，通过这个类型值我们可以创建不同的布局，而这个子在oncreateView中，所以我们根据布局要设置多个viewHolder，最好的方法是把他抽象出来，把应该写在onBindViewHolder里面的东西抽象成一个方法，到时候可以使用向上转型的方法一句话去实现。
     */
    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType()==Msg.TYPE_RECEIVED){
            return Msg.TYPE_RECEIVED;
        }else {
            return Msg.TYPE_SEND;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg=list.get(position);
        //通过Type来判断是左边还是右边 然后将一边隐藏了
        if (msg.getType()==Msg.TYPE_SEND){
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
            holder.right.setText(msg.getContent());
        }
        if (msg.getType()==Msg.TYPE_RECEIVED){
            holder.right.setVisibility(View.GONE);
            holder.left.setVisibility(View.VISIBLE);
            holder.left.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


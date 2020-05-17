package com.example.food.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.food.R;
import com.example.food.adapter.BaseAdapter;
import com.example.food.adapter.BaseHolder;
import com.example.food.bean.Message;

import java.util.List;


public class MessageAdapter extends BaseAdapter<Message> {
    private Callback callback;

    public MessageAdapter(Context context, List<Message> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public MessageAdapter(Context context, List<Message> datas, int[] layoutIds) {
        super(context, datas, layoutIds);
    }

    public void setCallback(Callback callback){
        this.callback=callback;
    }


    @Override
    protected void onBindData(BaseHolder baseHolder, final Message message, int postion) {
       TextView tvName=baseHolder.getView(R.id.tv_name);
       TextView tvAdr=baseHolder.getView(R.id.tv_adr);
       baseHolder.getItemView().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               callback.OnItemClick(message);
           }
       });
    }


    public interface Callback{
        void OnItemClick(Message message);
    }
}

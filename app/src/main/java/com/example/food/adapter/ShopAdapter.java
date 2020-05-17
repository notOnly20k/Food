package com.example.food.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food.R;
import com.example.food.adapter.BaseAdapter;
import com.example.food.adapter.BaseHolder;
import com.example.food.bean.Shop;

import java.util.List;



public class ShopAdapter extends BaseAdapter<Shop> {
    private Callback callback;

    public ShopAdapter(Context context, List<Shop> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public ShopAdapter(Context context, List<Shop> datas, int[] layoutIds) {
        super(context, datas, layoutIds);
    }

    public void setCallback(Callback callback){
        this.callback=callback;
    }


    @Override
    protected void onBindData(BaseHolder baseHolder, final Shop shop, int postion) {
       TextView tvName=baseHolder.getView(R.id.tv_name);
       TextView tvAdr=baseHolder.getView(R.id.tv_adr);
       ImageView imgFav=baseHolder.getView(R.id.img_fav);
       ImageView imgDisFav=baseHolder.getView(R.id.img_disfav);
       imgFav.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               imgFav.setVisibility(View.GONE);
               imgDisFav.setVisibility(View.VISIBLE);
           }
       });
        imgDisFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDisFav.setVisibility(View.GONE);
                imgFav.setVisibility(View.VISIBLE);
            }
        });
       baseHolder.getItemView().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               callback.OnItemClick(shop);
           }
       });
    }


    public interface Callback{
        void OnItemClick(Shop shop);
    }
}

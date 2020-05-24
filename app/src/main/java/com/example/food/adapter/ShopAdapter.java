package com.example.food.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.food.R;
import com.example.food.adapter.BaseAdapter;
import com.example.food.adapter.BaseHolder;
import com.example.food.bean.Constant;
import com.example.food.bean.Shop;
import com.example.food.bean.User;
import com.example.food.utils.PreferencesUtil;
import com.squareup.picasso.Picasso;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ShopAdapter extends BaseAdapter<Shop> {
    private Callback callback;
    private List<String> favList = new ArrayList<>();

    public ShopAdapter(Context context, List<Shop> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public ShopAdapter(Context context, List<Shop> datas, int[] layoutIds) {
        super(context, datas, layoutIds);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    protected void onBindData(BaseHolder baseHolder, final Shop shop, int postion) {
        TextView tvName = baseHolder.getView(R.id.tv_name);
        TextView tvAdr = baseHolder.getView(R.id.tv_adr);
        TextView tvPrice = baseHolder.getView(R.id.tv_price);
        TextView tvRank = baseHolder.getView(R.id.tv_rank);
        ImageView imgFav = baseHolder.getView(R.id.img_fav);
        ImageView imgDisFav = baseHolder.getView(R.id.img_disfav);
        ImageView img = baseHolder.getView(R.id.img);
        Picasso.with(context) //
                .load(shop.getPicUrl()) //加载地址
                .placeholder(R.drawable.shop)
                //占位图
                .error(R.drawable.shop) //加载失败的图
                .fit() //充满
                .into(img);//加载到的ImageView 

//        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.shop);
//        Glide.with(context)
//                .load(shop.getPicUrl())
//                .apply(options)
//                .into(imgDisFav);
        TagFlowLayout tagFlowLayout = baseHolder.getView(R.id.flowlayout);
        final LayoutInflater mInflater = LayoutInflater.from(context);
        tagFlowLayout.setAdapter(new TagAdapter<String>(shop.getType().split(";")) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag,
                        tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        tvName.setText(shop.getName());
        tvAdr.setText(shop.getAddress());
        tvPrice.setText("单价："+shop.getPrice()+"元/人");
        tvRank.setText("评分："+shop.getRank());
        imgFav.setVisibility(View.GONE);
        imgDisFav.setVisibility(View.VISIBLE);

        for (int i = 0; i < favList.size(); i++) {
            if (favList.get(i).equals(shop.getName())) {
                imgDisFav.setVisibility(View.GONE);
                imgFav.setVisibility(View.VISIBLE);
            }
        }
        imgFav.setOnClickListener(v -> {
            imgFav.setVisibility(View.GONE);
            imgDisFav.setVisibility(View.VISIBLE);
            callback.OnFavClick(shop, false);
        });
        imgDisFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDisFav.setVisibility(View.GONE);
                imgFav.setVisibility(View.VISIBLE);
                callback.OnFavClick(shop, true);
            }
        });
        baseHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.OnItemClick(shop);
            }
        });
    }

    public void notifyShopFav(List<String> names) {
        this.favList = names;
    }

//    @Override
//    public void setData(List<Shop> datas) {
//
//
//        super.setData(datas);
//    }

    public interface Callback {
        void OnItemClick(Shop shop);

        void OnFavClick(Shop shop, boolean fav);
    }
}

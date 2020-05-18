package com.example.food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food.R;
import com.example.food.adapter.BaseAdapter;
import com.example.food.adapter.BaseHolder;
import com.example.food.bean.Constant;
import com.example.food.bean.Shop;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.StringReader;
import java.util.ArrayList;
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
        ImageView imgFav = baseHolder.getView(R.id.img_fav);
        ImageView imgDisFav = baseHolder.getView(R.id.img_disfav);
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


    public interface Callback {
        void OnItemClick(Shop shop);

        void OnFavClick(Shop shop, boolean fav);
    }
}

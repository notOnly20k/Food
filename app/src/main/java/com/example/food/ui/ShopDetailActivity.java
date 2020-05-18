package com.example.food.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.bean.Message;
import com.example.food.adapter.MessageAdapter;
import com.example.food.R;
import com.example.food.bean.Shop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopDetailActivity extends BaseActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_adr)
    TextView tvAdr;
    @BindView(R.id.rec_message)
    RecyclerView recMessage;
    @BindView(R.id.img_fav)
    ImageView imgFav;
    @BindView(R.id.img_disfav)
    ImageView imgDisfav;
    private Shop shop;

    MessageAdapter messageAdapter;

    List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        setBackButton(true);
        ButterKnife.bind(this);
        shop = (Shop) getIntent().getExtras().getSerializable("shop");
        tvName.setText(shop.getName());
        tvAdr.setText(shop.getAddress());

        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFav.setVisibility(View.GONE);
                imgDisfav.setVisibility(View.VISIBLE);
            }
        });
        imgDisfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDisfav.setVisibility(View.GONE);
                imgFav.setVisibility(View.VISIBLE);
            }
        });
        messageAdapter = new MessageAdapter(this, messages, R.layout.item_message);
        messageAdapter.setCallback(new MessageAdapter.Callback() {
            @Override
            public void OnItemClick(Message message) {

            }
        });
        recMessage.setAdapter(messageAdapter);
        recMessage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getData();
    }

    private void getData() {
        messages.clear();
        messages.add(new Message());
        messages.add(new Message());
        messages.add(new Message());
        messages.add(new Message());
        messages.add(new Message());
        messageAdapter.setData(messages);
    }
}

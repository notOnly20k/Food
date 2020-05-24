package com.example.food.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.adapter.MessageAdapter;
import com.example.food.bean.Message;
import com.example.food.bean.Shop;
import com.example.food.bean.UserFav;
import com.example.food.dao.AppDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.card2)
    CardView card2;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.btn_comment)
    Button btnComment;
    @BindView(R.id.card3)
    LinearLayout card3;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    private Shop shop;

    MessageAdapter messageAdapter;

    List<Message> messages = new ArrayList<>();
    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        appDatabase = initAppDatabase();
        setBackButton(true);
        ButterKnife.bind(this);
        shop = (Shop) getIntent().getExtras().getSerializable("shop");
        tvName.setText(shop.getName());
        tvAdr.setText(shop.getAddress());
        tvPrice.setText("单价："+shop.getPrice()+"元/人");
        tvRank.setText("评分："+shop.getRank());
        Picasso.with(this) //
                .load(shop.getPicUrl()) //加载地址
                .placeholder(R.drawable.shop)
                //占位图
                .error(R.drawable.shop) //加载失败的图
                .fit() //充满
                .into(img);//加载到的ImageView 

        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFav.setVisibility(View.GONE);
                imgDisfav.setVisibility(View.VISIBLE);
                OnFavClick(shop, false);
            }
        });
        imgDisfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDisfav.setVisibility(View.GONE);
                imgFav.setVisibility(View.VISIBLE);
                OnFavClick(shop, true);
            }
        });
        messageAdapter = new MessageAdapter(this, messages, R.layout.item_message);
        recMessage.setAdapter(messageAdapter);
        recMessage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        btnComment.setOnClickListener(v -> {
            if (!etComment.getText().toString().isEmpty()) {
                Message message = new Message();
                message.setContent(etComment.getText().toString());
                message.setDate(new Date());
                message.setShopNmae(shop.getName());
                message.setUser(getCurrentUser());
                compositeDisposable.add(appDatabase.messageDao().insertMessage(message)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> {
                            showToast("操作失败");
                        })
                        .subscribe(() -> {
                                    getData();
                                    showToast("评论成功");
                                },
                                throwable -> {
                                    showToast("操作失败");
                                    Log.e(TAG, "操作失败", throwable);
                                })
                );
            }
        });

        compositeDisposable.add(appDatabase.shopDao().getUserFavShopId(getCurrentUser().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    showToast("操作失败");
                })
                .subscribe(names -> {
                            for (int i = 0; i < names.size(); i++) {
                                if (shop.getName().equals(names.get(i))) {
                                    imgDisfav.setVisibility(View.GONE);
                                    imgFav.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        throwable -> {
                            showToast("操作失败");
                            Log.e(TAG, "操作失败", throwable);
                        })
        );
        getData();
    }

    public void OnFavClick(Shop shop, boolean fav) {
        UserFav userFav = new UserFav();
        userFav.setName(shop.getName());
        userFav.setUserId(getCurrentUser().getUserId());
        Completable completable;
        Log.e(TAG, fav + "");
        if (fav) {
            completable = appDatabase.shopDao().insertFav(userFav);

        } else {
            completable = appDatabase.shopDao().deleteFav(userFav.getName(), userFav.getUserId());
        }
        compositeDisposable.add(completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    showToast("操作失败");
                })
                .subscribe(() -> {
                            if (fav) {
                                showToast("收藏成功");
                            } else {
                                showToast("取消成功");
                            }
                        },
                        throwable -> {
                            showToast("操作失败");
                            Log.e(TAG, "操作失败", throwable);
                        })
        );
    }

    private void getData() {
        compositeDisposable.add(appDatabase.messageDao().getMessagesByShopId(shop.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    showToast("操作失败");
                })
                .subscribe(list -> {
                            messages.clear();
                            messages.addAll(list);
                            messageAdapter.setData(messages);
                        },
                        throwable -> {
                            showToast("操作失败");
                            Log.e(TAG, "操作失败", throwable);
                        })
        );
    }
}

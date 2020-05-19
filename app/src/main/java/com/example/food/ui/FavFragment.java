package com.example.food.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food.R;
import com.example.food.adapter.ShopAdapter;
import com.example.food.bean.Shop;
import com.example.food.bean.UserFav;
import com.example.food.dao.AppDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.swip)
    SwipeRefreshLayout swip;

    private ShopAdapter shopAdapter;
    @BindView(R.id.rec_home)
    RecyclerView recHome;
    private ArrayList<Shop> shops = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavFragment newInstance(String param1, String param2) {
        FavFragment fragment = new FavFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    AppDatabase appDatabase;

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appDatabase = initAppDatabase();
        shopAdapter = new ShopAdapter(getContext(), shops, R.layout.item_shop);
        shopAdapter.setCallback(new ShopAdapter.Callback() {
            @Override
            public void OnItemClick(Shop shop) {
                Intent intent = new Intent(getContext(), ShopDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shop", (Serializable) shop);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
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
                                    refresh();
                                },
                                throwable -> {
                                    showToast("操作失败");
                                    Log.e(TAG, "操作失败", throwable);
                                })
                );
            }
        });
        recHome.setAdapter(shopAdapter);
        recHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getData();
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    public void getData(){
        compositeDisposable.add(appDatabase.shopDao().getUserFav(getCurrentUser().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    showToast("操作失败");
                })
                .subscribe(data -> {
                            shops.clear();
                            shops.addAll(data.getShops());
                            Log.e(TAG, data.toString());
                            refresh();
                            swip.setRefreshing(false);
                        },
                        throwable -> {
                            showToast("操作失败");
                            Log.e(TAG, "操作失败", throwable);
                            swip.setRefreshing(false);
                        })
        );
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    public void refresh() {
        compositeDisposable.add(appDatabase.shopDao().getUserFavShopId(getCurrentUser().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    showToast("操作失败");
                })
                .subscribe(names -> {
                            Log.e(TAG, names.toString());
                            shopAdapter.notifyShopFav(names);
                            shopAdapter.setData(shops);
                        },
                        throwable -> {
                            showToast("操作失败");
                            Log.e(TAG, "操作失败", throwable);
                        })
        );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}

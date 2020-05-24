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
import com.example.food.bean.Shop;
import com.example.food.adapter.ShopAdapter;
import com.example.food.bean.UserFav;
import com.example.food.dao.AppDatabase;
import com.example.food.utils.RxBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ShopAdapter shopAdapter;

    @BindView(R.id.swip)
    SwipeRefreshLayout swip;
    @BindView(R.id.rec_home)
    RecyclerView recHome;
    private ArrayList<Shop> shops = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Shop>favList=new ArrayList<>();

    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
         appDatabase=initAppDatabase();
        shopAdapter = new ShopAdapter(getContext(), shops, R.layout.item_shop);
        shopAdapter.setCallback(new ShopAdapter.Callback() {
            @Override
            public void OnItemClick(Shop shop) {
                Intent intent=new Intent(getContext(),ShopDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("shop",(Serializable)shop);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void OnFavClick(Shop shop, boolean fav) {
                UserFav userFav=new UserFav();
                userFav.setName(shop.getName());
                userFav.setUserId(getCurrentUser().getUserId());
                Completable completable;
                Log.e(TAG,fav+"");
                if (fav){
                   completable= appDatabase.shopDao().insertFav(userFav);

                }else {
                    completable=appDatabase.shopDao().deleteFav(userFav.getName(),userFav.getUserId());
                }
                compositeDisposable.add(completable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> {
                            showToast("操作失败");
                        })
                        .subscribe(() -> {
                                    if (fav){
                                        showToast("收藏成功");
                                    }else {
                                        showToast("取消成功");
                                    }
                                },
                                throwable -> {
                                    showToast("操作失败");
                                    Log.e(TAG,"操作失败",throwable);
                                })
                );
            }
        });
        recHome.setAdapter(shopAdapter);
        recHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RxBus.get().toObservable()
                .map(o -> (List<Shop>) o)
                .doOnSubscribe(it->compositeDisposable.add(it))
                .subscribe(list -> {
                    if (list != null) {
                        shops.clear();
                        shops.addAll(list);
                        refresh();
                    }
                });

        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }



    public void refresh(){
        compositeDisposable.add(appDatabase.shopDao().getUserFav(getCurrentUser().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    showToast("操作失败");
                })
                .subscribe(favs -> {
                            rankShop(shops,favs.getShops());
                        },
                        throwable -> {
                            showToast("操作失败");
                            Log.e(TAG,"操作失败",throwable);
                            swip.setRefreshing(false);
                        })
        );
    }

    public void rankShop(List<Shop>shopList,List<Shop>favList){
        List<String>names=new ArrayList<>();
        Set<String>tags=new HashSet<>();
        for (int i = 0; i < shopList.size(); i++) {
            Shop shop=shopList.get(i);
            shop.setWeight(0);
            if (getCurrentUser().getFav().get(0).equals(shop.getType())){
                shop.setWeight(shop.getWeight()+10);
            }
            int jobWeight=0;
            switch (getCurrentUser().getJob()){
                case "学生":
                    jobWeight=30;
                    break;
                case "工人":
                    jobWeight=40;
                    break;
                case "白领":
                    jobWeight=60;
                    break;
                case "吃货":
                    jobWeight=100;
                    break;
            }
            if (Integer.parseInt(shop.getPrice())<=jobWeight){
                shop.setWeight(shop.getWeight()+8);
            }

            for (int j = 0; j < favList.size(); j++) {
                if (shop.getName().equals(favList.get(j).getName())){
                    shop.setWeight(shop.getWeight()+10);
                }
                names.add(favList.get(j).getName());
                tags.add(favList.get(j).getType());
            }

            for (String str : tags) {
               if (str.equals(shop.getType()))
                   shop.setWeight(shop.getWeight()+10);
            }

        }
        Collections.sort(shops, new Comparator<Shop>() {
            @Override
            public int compare(Shop shop, Shop t1) {

                return -(shop.getWeight()-t1.getWeight());
            }
        });
        Log.e("tag",shops.toString());
        shopAdapter.notifyShopFav(names);
        shopAdapter.setData(shops);
        swip.setRefreshing(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}

package com.example.food.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.food.bean.Shop;
import com.example.food.bean.User;
import com.example.food.bean.UserAllShop;
import com.example.food.bean.UserFav;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertShop(Shop shop);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllShops(List<Shop> userEntities);

    @Delete
    void deleteShop(Shop shop);

    @Query("SELECT * FROM Shop")
    Flowable<List<Shop>> getAllShops();

    @Query("SELECT * FROM User WHERE userId = :userId")
    Flowable<UserAllShop> getUserFav(int userId);

    @Transaction
    @Query("SELECT name FROM userfav WHERE userId = :userId")
    Flowable<List<String>> getUserFavShopId(int userId);


    @Insert
    Completable insertFav(UserFav userFavs);

    @Query("delete  from UserFav where name = :name and userId = :userId")
    Completable deleteFav(String name,int userId);
}

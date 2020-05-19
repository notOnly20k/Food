package com.example.food.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.food.bean.Message;
import com.example.food.bean.Shop;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMessage(Message message);

    @Query("SELECT * FROM Message where shopNmae = :shopName")
    Flowable<List<Message>> getMessagesByShopId(String shopName);

}

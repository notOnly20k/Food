package com.example.food.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.food.bean.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(User userEntity);

    @Insert
    Completable insertAll(List<User> userEntities);

    @Delete
    void delete(User userEntity);

    @Delete
    void deleteAll(List<User> userEntities);

    @Update
    Completable update(User userEntity);

    @Query("SELECT * FROM User")
    Flowable<List<User>> getAll();

    @Query("SELECT * FROM User WHERE name  = :name and pwd = :pwd")
    Flowable<User> login(String name,String pwd);
}

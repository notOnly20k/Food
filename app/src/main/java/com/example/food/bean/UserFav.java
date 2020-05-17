package com.example.food.bean;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "user_id",onDelete = CASCADE),
        @ForeignKey(entity = Shop.class, parentColumns = "uid", childColumns = "shop_id",onDelete = CASCADE)},indices = {@Index(value = "user_id"),@Index(value = "shop_id")})
public class UserFav {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private int user_id;
    private int shop_id;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    @Override
    public String toString() {
        return "UserFav{" +
                "uid=" + uid +
                ", user_id=" + user_id +
                ", shop_id=" + shop_id +
                '}';
    }
}

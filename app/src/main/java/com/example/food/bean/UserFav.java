package com.example.food.bean;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId",onDelete = CASCADE),
        @ForeignKey(entity = Shop.class, parentColumns = "name", childColumns = "name",onDelete = CASCADE)},indices = {@Index(value = "userId"),@Index(value = "name")})
public class UserFav {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private int userId;
    private String name;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

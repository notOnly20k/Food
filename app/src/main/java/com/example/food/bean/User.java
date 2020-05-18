package com.example.food.bean;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.food.utils.StringListConverters;

import java.io.Serializable;
import java.util.List;

@Entity
@TypeConverters(StringListConverters.class)
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String name;
    private String pwd;
    private List<String>fav;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public List<String> getFav() {
        return fav;
    }

    public void setFav(List<String> fav) {
        this.fav = fav;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", fav=" + fav +
                '}';
    }
}

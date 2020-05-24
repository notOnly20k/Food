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
    private String birth;
    private String sex;
    private String job;
    private List<String>fav;

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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
                ", birth='" + birth + '\'' +
                ", sex='" + sex + '\'' +
                ", job='" + job + '\'' +
                ", fav=" + fav +
                '}';
    }
}

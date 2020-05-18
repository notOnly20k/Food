package com.example.food.bean;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverters;

import com.example.food.utils.DateConverters;
import com.example.food.utils.StringListConverters;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity
@TypeConverters(DateConverters.class)
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded
    private User user;
    private int shopNmae;
    private Date date;
    private String content;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getShopNmae() {
        return shopNmae;
    }

    public void setShopNmae(int shopNmae) {
        this.shopNmae = shopNmae;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uid=" + uid +
                ", user=" + user +
                ", shopNmae=" + shopNmae +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }
}

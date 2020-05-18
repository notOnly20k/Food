package com.example.food.bean;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserAllShop {
    @Embedded
    private User user;
    @Relation(parentColumn = "userId",entityColumn = "name",associateBy = @Junction(UserFav.class))
    private List<Shop>shops;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}

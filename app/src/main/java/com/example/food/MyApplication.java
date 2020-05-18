package com.example.food;

import android.app.Application;

import com.example.food.utils.PreferencesUtil;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PreferencesUtil.getInstance().init(this);
    }
}

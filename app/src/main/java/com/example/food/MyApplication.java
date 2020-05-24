package com.example.food;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.example.food.utils.PreferencesUtil;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        PreferencesUtil.getInstance().init(this);
    }
}

package com.example.food.ui;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.food.bean.User;
import com.example.food.dao.AppDatabase;
import com.example.food.utils.PreferencesUtil;

import io.reactivex.disposables.CompositeDisposable;

public class BaseFragment extends Fragment {
    String TAG = this.getClass().getName();


    CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void showToast(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }

    protected AppDatabase initAppDatabase(){
        AppDatabase  appDatabase= Room.databaseBuilder(getContext(),AppDatabase.class,"food.db").build();
        return appDatabase;
    }

    protected User getCurrentUser(){
        return (User) PreferencesUtil.getInstance().getParam("user", null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}

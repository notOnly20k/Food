package com.example.food.ui;

import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.food.bean.User;
import com.example.food.dao.AppDatabase;
import com.example.food.utils.PreferencesUtil;

import io.reactivex.disposables.CompositeDisposable;

public class BaseActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();


    CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected void setBackButton(boolean b){
        if (b){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    protected AppDatabase initAppDatabase(){
        AppDatabase  appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"food.db").build();
         return appDatabase;
    }

    protected User getCurrentUser(){
        return (User) PreferencesUtil.getInstance().getParam("user", null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

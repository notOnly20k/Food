package com.example.food.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.food.R;
import com.example.food.bean.Constant;
import com.example.food.bean.User;
import com.example.food.dao.AppDatabase;
import com.example.food.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UersInfoActivity extends BaseActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etTel)
    EditText etTel;
    @BindView(R.id.btn_save)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uers_info);
        ButterKnife.bind(this);
        setBackButton(true);
        User user=getCurrentUser();
        etName.setText(user.getName());
        etTel.setText(user.getPwd());
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String pwd = etTel.getText().toString();
            if (name.isEmpty() || pwd.isEmpty()) {
                showToast("请完善个人信息");
            } else {
                user.setName(name);
                user.setPwd(pwd);
                AppDatabase appDatabase = initAppDatabase();
                appDatabase.userDao()
                        .update(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> compositeDisposable.add(disposable))
                        .subscribe(() -> {
                                    PreferencesUtil.getInstance().saveParam("user", user);
                                    showToast("操作成功");
                                },
                                throwable -> {
                                    showToast("操作失败");
                                    Log.e(TAG, "操作失败", throwable);
                                });
            }
        });

    }
}

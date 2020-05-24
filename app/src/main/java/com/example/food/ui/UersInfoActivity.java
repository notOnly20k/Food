package com.example.food.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.food.R;
import com.example.food.bean.User;
import com.example.food.dao.AppDatabase;
import com.example.food.utils.PreferencesUtil;

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
    @BindView(R.id.btn_date)
    Button btnDate;
    @BindView(R.id.tv_birth)
    TextView tvBirth;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_woman)
    RadioButton rbWoman;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uers_info);
        ButterKnife.bind(this);
        setBackButton(true);
        User user = getCurrentUser();
        etName.setText(user.getName());
        etTel.setText(user.getPwd());
        tvBirth.setText(user.getBirth());
        if (user.getSex().equals("man")){
            rgSex.check(R.id.rb_man);
        }else {
            rgSex.check(R.id.rb_woman);
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String pwd = etTel.getText().toString();
            String birth =tvBirth.getText().toString();
            String sex=rgSex.getCheckedRadioButtonId()==R.id.rb_man?"man":"woman";
            if (name.isEmpty() || pwd.isEmpty()||birth.isEmpty()||sex.isEmpty()) {
                showToast("请完善个人信息");
            } else {
                user.setName(name);
                user.setPwd(pwd);
                user.setSex(sex);
                user.setBirth(birth);
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

package com.example.food.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.food.R;
import com.example.food.bean.Constant;
import com.example.food.bean.User;
import com.example.food.dao.AppDatabase;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etTel)
    EditText etTel;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.btn_save)
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater mInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_register);
        setBackButton(true);
        ButterKnife.bind(this);

        flowlayout.setAdapter(new TagAdapter<String>(Constant.TYPEARRY)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag,
                        flowlayout, false);
                tv.setText(s);
                return tv;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                String name=etName.getText().toString();
                String tel=etTel.getText().toString();
                if (name.isEmpty()||tel.isEmpty()){
                    showToast("请完善个人信息");
                }else {
                    User user=new User();
                    List<String>selectfav=new ArrayList<>();
                    Iterator it = flowlayout.getSelectedList().iterator();
                    while (it.hasNext()) {
                        selectfav.add(Constant.TYPEARRY.get((Integer) it.next()));
                    }

                    user.setFav(selectfav);
                     AppDatabase appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"food.db").build();
                     appDatabase.userDao()
                             .insert(user)
                             .subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                             .doOnSubscribe(disposable -> compositeDisposable.add(disposable))
                             .subscribe(lon -> {
                                         showToast("操作成功");
                                     },
                                     throwable -> {
                                         showToast("操作失败");
                                         Log.e(TAG,"操作失败",throwable);
                                     });
                }
            }
        });
    }


}

package com.example.food.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.food.R;
import com.example.food.bean.Constant;
import com.example.food.bean.User;
import com.example.food.dao.AppDatabase;
import com.example.food.utils.PreferencesUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserTagActivity extends BaseActivity {

    @BindView(R.id.flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.btn_save)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tag);
        ButterKnife.bind(this);
        setBackButton(true);
        User user=getCurrentUser();
        final LayoutInflater mInflater = LayoutInflater.from(this);
        TagAdapter adapter = new TagAdapter<String>(Constant.TYPEARRY) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag,
                        flowlayout, false);
                tv.setText(s);
                return tv;
            }
        };
        flowlayout.setAdapter(adapter);
        Set set = new HashSet();
        for (int i = 0; i < user.getFav().size(); i++) {
            for (int j = 0; j < Constant.TYPEARRY.size(); j++) {
                if (user.getFav().get(i).equals(Constant.TYPEARRY.get(j))) {
                    set.add(j);
                }
            }
        }
        adapter.setSelectedList(set);

        btnSave.setOnClickListener(v -> {
                List<String> selectfav = new ArrayList<>();
                Iterator it = flowlayout.getSelectedList().iterator();
                while (it.hasNext()) {
                    selectfav.add(Constant.TYPEARRY.get((Integer) it.next()));
                }
                user.setFav(selectfav);
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
        });

    }
}

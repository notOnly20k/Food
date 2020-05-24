package com.example.food.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import cn.addapp.pickers.picker.DatePicker;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    @BindView(R.id.btn_date)
    Button btnDate;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_woman)
    RadioButton rbWoman;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.flowlayout2)
    TagFlowLayout flowlayout2;
    @BindView(R.id.tv_birth)
    TextView tvBirth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater mInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_register);
        setBackButton(true);
        ButterKnife.bind(this);

        flowlayout.setAdapter(new TagAdapter<String>(Constant.TYPEARRY) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag,
                        flowlayout, false);
                tv.setText(s);
                return tv;
            }
        });

        flowlayout2.setAdapter(new TagAdapter<String>(Constant.JOBARRY) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag,
                        flowlayout2, false);
                tv.setText(s);
                return tv;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String pwd = etTel.getText().toString();
                String birth =tvBirth.getText().toString();
                String sex=rgSex.getCheckedRadioButtonId()==R.id.rb_man?"man":"woman";
                String job=flowlayout2.getSelectedList().iterator().hasNext()?Constant.JOBARRY.get((Integer) flowlayout2.getSelectedList().iterator().next()):"";
//                String job=flowlayout2.getSelectedList()
                if (name.isEmpty() || pwd.isEmpty()||birth.isEmpty()||sex.isEmpty()||job.isEmpty()) {
                    showToast("请完善个人信息");
                } else {
                    User user = new User();
                    List<String> selectfav = new ArrayList<>();
                    Iterator it = flowlayout.getSelectedList().iterator();
                    while (it.hasNext()) {
                        selectfav.add(Constant.TYPEARRY.get((Integer) it.next()));
                    }

                    user.setFav(selectfav);
                    user.setName(name);
                    user.setPwd(pwd);
                    user.setBirth(birth);
                    user.setSex(sex);
                    user.setJob(job);
                    AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "food.db").build();
                    appDatabase.userDao()
                            .insert(user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(disposable -> compositeDisposable.add(disposable))
                            .subscribe(() -> {
                                        showToast("操作成功");
                                        Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent2);
                                        finish();
                                    },
                                    throwable -> {
                                        showToast("操作失败");
                                        Log.e(TAG, "操作失败", throwable);
                                    });
                }
            }
        });

        btnDate.setOnClickListener(v -> {
            onYearMonthDayPicker();
        });

        rgSex.check(R.id.rb_man);
    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(this);
        picker.setTopPadding(15);
        picker.setRangeStart(1900, 1, 1);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(2020, 1, 1);
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tvBirth.setText(year+"-"+month+"-"+day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }


}

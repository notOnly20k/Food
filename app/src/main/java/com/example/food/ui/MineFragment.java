package com.example.food.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etTel)
    EditText etTel;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private User user;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        user = getCurrentUser();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName.setText(user.getName());
        etTel.setText(user.getPwd());
        final LayoutInflater mInflater = LayoutInflater.from(getContext());
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
            String name = etName.getText().toString();
            String pwd = etTel.getText().toString();
            if (name.isEmpty() || pwd.isEmpty()) {
                showToast("请完善个人信息");
            } else {
                List<String> selectfav = new ArrayList<>();
                Iterator it = flowlayout.getSelectedList().iterator();
                while (it.hasNext()) {
                    selectfav.add(Constant.TYPEARRY.get((Integer) it.next()));
                }
                user.setFav(selectfav);
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

        btnLogout.setOnClickListener(v->{
            PreferencesUtil.getInstance().remove("user");
            Intent intent2 = new Intent(getContext(), LoginActivity.class);
            startActivity(intent2);
            getActivity().finish();
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}

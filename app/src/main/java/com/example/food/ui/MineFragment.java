package com.example.food.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.food.R;
import com.example.food.bean.User;
import com.example.food.utils.PreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_fav)
    TextView tvFav;
    @BindView(R.id.tv_job)
    TextView tvJob;

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

        tvFav.setOnClickListener(v -> {
            Intent intent2 = new Intent(getContext(), UserTagActivity.class);
            startActivity(intent2);
        });

        tvInfo.setOnClickListener(v -> {
            Intent intent2 = new Intent(getContext(), UersInfoActivity.class);
            startActivity(intent2);
        });

        tvJob.setOnClickListener(v->{
            Intent intent2 = new Intent(getContext(), UserJobActivity.class);
            startActivity(intent2);
        });
        btnLogout.setOnClickListener(v -> {
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

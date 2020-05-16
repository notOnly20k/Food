package com.example.food;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RxPermissions rxPermissions;
    boolean premission = false;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(this);
        getPremission();
//        if (PreferencesUtil.getInstance().getParam("user", null) != null) {
//            Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent2);
//        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
    }

    @SuppressLint("CheckResult")
    private void getPremission() {

        rxPermissions.requestEach(Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.LOCATION_HARDWARE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        compositeDisposable.add(disposable);
                    }
                })
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            premission = true;
                            return;
                        }
                        if (permission.shouldShowRequestPermissionRationale) {
                            premission = false;
                            Toast.makeText(LoginActivity.this, "请同意权限", Toast.LENGTH_SHORT).show();
                            getPremission();
                            return;
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void login(View view) {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent2);
    }

    public void register(View view) {
        Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }
}

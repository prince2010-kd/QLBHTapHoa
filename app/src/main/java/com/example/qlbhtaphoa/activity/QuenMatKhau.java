package com.example.qlbhtaphoa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.retrofit.ApiBanHang;
import com.example.qlbhtaphoa.retrofit.RetrofitClient;
import com.example.qlbhtaphoa.utils.Utils;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuenMatKhau extends AppCompatActivity {
    EditText mail;
    AppCompatButton btl_quen_mat_khau;
    ApiBanHang apiBanHang;
    CompositeDisposable disposable = new CompositeDisposable();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        anhxa();
        initControl();
    }

    private void initControl() {
        // Xử lý nút ấn
        btl_quen_mat_khau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_mail = mail.getText().toString().trim();
                if(TextUtils.isEmpty(str_mail)) {
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập mail", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    disposable.add(apiBanHang.resetPass(str_mail)
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()) {
                                            Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.setVisibility(View.VISIBLE);
                                    }, throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                            ));
                }
            }
        });
    }

    private void anhxa() {
        mail = findViewById(R.id.quen_mat_khau);
        btl_quen_mat_khau = findViewById(R.id.btl_quen_mat_khau);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        progressBar = findViewById(R.id.progress);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
package com.example.qlbhtaphoa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.retrofit.ApiBanHang;
import com.example.qlbhtaphoa.retrofit.RetrofitClient;
import com.example.qlbhtaphoa.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText mail, pass, nhap_pass, txt_dien_thoai, txt_user_name;
    Button button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        anhxaView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangki();
            }
        });
    }

    private void dangki() {
        String str_mail = mail.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_nhap_pass = nhap_pass.getText().toString().trim();
        String str_dienthoai = txt_dien_thoai.getText().toString().trim();
        String str_username = txt_user_name.getText().toString().trim();
        if(TextUtils.isEmpty(str_mail)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập mail", Toast.LENGTH_SHORT ).show();
        } else if(TextUtils.isEmpty(str_username)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên người dùng", Toast.LENGTH_SHORT ).show();
        } else if(TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT ).show();
        } else if(TextUtils.isEmpty(str_nhap_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập lại mật khẩu", Toast.LENGTH_SHORT ).show();
        } else if(TextUtils.isEmpty(str_dienthoai)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT ).show();
        } else {
            if(str_pass.equals(str_nhap_pass)) {
                compositeDisposable.add(apiBanHang.dangki(str_mail,str_pass, str_username, str_dienthoai)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()) {
                                        Utils.user.setMail(str_mail);
                                        Utils.user.setPassword(str_pass);
                                        Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT ).show();
                                    }
                                }, throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT ).show();
                                }
                        ));
            } else {
                Toast.makeText(getApplicationContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    private void anhxaView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        mail = findViewById(R.id.txt_mail_dang_ki);
        pass = findViewById(R.id.txt_pass_dang_ki);
        nhap_pass = findViewById(R.id.txt_pass2_dang_ki);
        txt_dien_thoai = findViewById(R.id.txt_dien_thoai);
        txt_user_name = findViewById(R.id.txt_user_name);
        button = findViewById(R.id.btl_dang_ki);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
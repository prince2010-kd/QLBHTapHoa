package com.example.qlbhtaphoa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.retrofit.ApiBanHang;
import com.example.qlbhtaphoa.retrofit.RetrofitClient;
import com.example.qlbhtaphoa.utils.Utils;
import com.google.android.gms.common.api.Api;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txt_dang_ki, txt_quen_mat_khau;
    EditText mail_dang_nhap, pass_dang_nhap;
    Button btl_dang_nhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        anhxaView();
        initControl();
    }

    private void initControl() {
        txt_dang_ki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DangKiActivity.class);
                startActivity(intent);
            }
        });
        // Xử lý quên mật khẩu
        txt_quen_mat_khau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QuenMatKhau.class);
                startActivity(intent);
            }
        });

        // Xử lý nút đăng nhập
        btl_dang_nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_mail = mail_dang_nhap.getText().toString().trim();
                String str_pass =  pass_dang_nhap.getText().toString().trim();
                if(TextUtils.isEmpty(str_mail)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập mail", Toast.LENGTH_SHORT ).show();
                } else if(TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên người dùng", Toast.LENGTH_SHORT ).show();
                } else {
                    Paper.book().write("mail", str_mail);
                    Paper.book().write("password", str_pass);
                    compositeDisposable.add(apiBanHang.dangnhap(str_mail, str_pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()) {
                                            Utils.user = userModel.getResult().get(0);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT ).show();
                                    }
                            ));
                }
            }
        });
    }

    private void anhxaView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txt_dang_ki = findViewById(R.id.txt_dang_ki);
        txt_quen_mat_khau = findViewById(R.id.txt_quen_mat_khau);
        mail_dang_nhap = findViewById(R.id.txt_dang_nhap_mail);
        pass_dang_nhap = findViewById(R.id.txt_dang_nhap_pass);
        btl_dang_nhap = findViewById(R.id.btl_dang_nhap);
        if(Paper.book().read("mail") != null  && Paper.book().read("password") != null){
            mail_dang_nhap.setText(Paper.book().read("mail"));
            pass_dang_nhap.setText(Paper.book().read("password"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user.getMail() != null && Utils.user.getPassword() != null) {
            mail_dang_nhap.setText(Utils.user.getMail());
            pass_dang_nhap.setText(Utils.user.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
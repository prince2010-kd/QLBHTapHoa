package com.example.qlbhtaphoa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.model.GioHang;
import com.example.qlbhtaphoa.model.SanPhamMoi;
import com.example.qlbhtaphoa.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView ten_sp, gia_sp, mo_ta;
    Button btl_them;
    ImageView img_hinh_anh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btl_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if(Utils.manggiohang.size() > 0) {
            boolean flag = false;
            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i < Utils.manggiohang.size(); i++) {
                if(Utils.manggiohang.get(i).getId_sp() == sanPhamMoi.getId()) {
                    Utils.manggiohang.get(i).setSo_luong(soLuong + Utils.manggiohang.get(i).getSo_luong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasp()) * Utils.manggiohang.get(i).getSo_luong();
                    Utils.manggiohang.get(i).setGia_sp(gia);
                    flag =true;
                }
            }
            if(flag == false ) {

                long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soLuong;
                GioHang gioHang = new GioHang();
                gioHang.setGia_sp(gia);
                gioHang.setSo_luong(soLuong);
                gioHang.setId_sp(sanPhamMoi.getId());
                gioHang.setTen_sp(sanPhamMoi.getTensp());
                gioHang.setHinh_anh_sp(sanPhamMoi.getHinhanh());
                Utils.manggiohang.add(gioHang);
            }
        }
        else {
            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soLuong;
            GioHang gioHang = new GioHang();
            gioHang.setGia_sp(gia);
            gioHang.setSo_luong(soLuong);
            gioHang.setId_sp(sanPhamMoi.getId());
            gioHang.setTen_sp(sanPhamMoi.getTensp());
            gioHang.setHinh_anh_sp(sanPhamMoi.getHinhanh());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem = 0 ;
        for(int i = 0; i< Utils.manggiohang.size(); i++) {
            totalItem = totalItem + Utils.manggiohang.get(i).getSo_luong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        sanPhamMoi =  sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        ten_sp.setText(sanPhamMoi.getTensp());
      //  gia_sp.setText(sanPhamMoi.getGiasp());
        mo_ta.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(img_hinh_anh);
        DecimalFormat decimalFormat  = new DecimalFormat("###,###,###");
        gia_sp.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+ "đ");
        Integer[] so = new Integer[] {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> integerArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(integerArrayAdapter);
    }

    private void initView() {
        ten_sp = findViewById(R.id.txt_ten_sp_chi_tiet);
        gia_sp = findViewById(R.id.txt_gia_sp);
        mo_ta = findViewById(R.id.txt_mo_ta_chi_tiet);
        btl_them = findViewById(R.id.btl_them);
        spinner = findViewById(R.id.spin);
        img_hinh_anh = findViewById(R.id.hinh_anh_chi_tiet);
        toolbar = findViewById(R.id.bar_chi_tiet);
        badge = findViewById(R.id.menu_sl);

        FrameLayout frameLayout = findViewById(R.id.far_chi_tiet);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if (Utils.manggiohang != null) {
            int totalItem = 0 ;
            for(int i = 0; i< Utils.manggiohang.size(); i++) {
                totalItem = totalItem + Utils.manggiohang.get(i).getSo_luong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null) {
            int totalItem = 0 ;
            for(int i = 0; i< Utils.manggiohang.size(); i++) {
                totalItem = totalItem + Utils.manggiohang.get(i).getSo_luong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}
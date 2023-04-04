package com.example.qlbhtaphoa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.adapter.GioHangAdapter;
import com.example.qlbhtaphoa.model.EventBus.TinhTong;
import com.example.qlbhtaphoa.model.GioHang;
import com.example.qlbhtaphoa.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView gio_hang_trong, tong_tien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btl_mua_hang;
    GioHangAdapter gioHangAdapter;
    List<GioHang> gioHangList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        anhxaView();
        initControl();
        tinhTongTien();
    }

    private void tinhTongTien() {
        long tongtiensp = 0;
        for (int i = 0 ; i < Utils.manggiohang.size(); i ++) {
            tongtiensp = tongtiensp + (Utils.manggiohang.get(i).getGia_sp() * Utils.manggiohang.get(i).getSo_luong());
        }
        DecimalFormat decimalFormat  = new DecimalFormat("###,###,###");
//        gia_sp.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+ "đ");
        tong_tien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.manggiohang.size() == 0) {
            gio_hang_trong.setVisibility(View.VISIBLE);

        } else {
            gioHangAdapter = new GioHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(gioHangAdapter);
        }
    }

    private void anhxaView() {
        gio_hang_trong = findViewById(R.id.txt_gio_hang_trong);
        toolbar = findViewById(R.id.too_gio_hang);
        recyclerView = findViewById(R.id.recy_gio_hang);
        tong_tien = findViewById(R.id.txt_tong_tien_gio_hang);
        btl_mua_hang = findViewById(R.id.btl_mua_hang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void tinhTien(TinhTong tinhTong) {
        if(tinhTong != null) {
            tinhTongTien();
        }
    }
}
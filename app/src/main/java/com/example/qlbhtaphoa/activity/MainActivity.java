package com.example.qlbhtaphoa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qlbhtaphoa.adapter.LoaiSanPham;
import com.example.qlbhtaphoa.adapter.SanPhamMoiAdapter;
import com.example.qlbhtaphoa.model.LoaiSp;
import com.example.qlbhtaphoa.model.SanPhamMoi;

import com.example.qlbhtaphoa.object.Photo;
import com.example.qlbhtaphoa.object.PhotoViewPage2Adap;
import com.example.qlbhtaphoa.retrofit.ApiBanHang;
import com.example.qlbhtaphoa.retrofit.RetrofitClient;
import com.example.qlbhtaphoa.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator3;
import com.example.qlbhtaphoa.R;
import com.nex3z.notificationbadge.NotificationBadge;

public class MainActivity extends AppCompatActivity {
    // Khai báo
    Toolbar toolbar;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    LoaiSanPham loaiSanPham;
    List<SanPhamMoi> sanPhamMoiModels;
    SanPhamMoiAdapter moiAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;

    private  ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private List<Photo> photoList;
    private List<LoaiSp> loaiSps;

    // Logic chuyển page
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager2.getCurrentItem() == photoList.size() -1 ) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(R.layout.activity_main);
        Anhxa();
        ActionBar();
        // Run photo
        PhotoViewPage2Adap adap = new PhotoViewPage2Adap(photoList);
        viewPager2.setAdapter(adap);
        circleIndicator3.setViewPager(viewPager2);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                    super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 2500);
            }
        });

        if(isConnect(this)) {
            getLoaiSanPham();
            getSanPhamMoi();
            geteventClick();
        } else {
            Toast.makeText(getApplicationContext(),"lỗi kết nối",Toast.LENGTH_SHORT).show();
        }
    }

    private void geteventClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent doanvat = new Intent(getApplicationContext(), DoAnVatActivity.class);
                        doanvat.putExtra("loai", 1);
                        startActivity(doanvat);
                        break;
                    case 2:
                        Intent nuocgiaikhat = new Intent(getApplicationContext(), DoAnVatActivity.class);
                        nuocgiaikhat.putExtra("loai", 2);
                        startActivity(nuocgiaikhat);
                        break;
                    case 5:
                        Intent quanli = new Intent(getApplicationContext(), QuanLiActivity.class);
                        startActivity(quanli);
                        break;
                    case 6:
                        Paper.book().delete("user");
                        Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangnhap);
                        break;
                }
            }
        });
    }

    private void getSanPhamMoi() {
        compositeDisposable.add(apiBanHang.getSanPhamMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                                sanPhamMoiModels = sanPhamMoiModel.getResult();
                                moiAdapter = new SanPhamMoiAdapter(getApplicationContext(), sanPhamMoiModels);
                                recyclerView.setAdapter(moiAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không có kết nối"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    // Khơỉ chạy toobar menu
    private  void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if(loaiSpModel.isSuccess()) {
                            loaiSps = loaiSpModel.getResult();
//                            loaiSps.add(new LoaiSp("Đăng xuất",""));
                                loaiSanPham = new LoaiSanPham(getApplicationContext(), loaiSps);
                                listView.setAdapter(loaiSanPham);
                            }
                        }
                )
        );
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.tooBarMainChinh);
        recyclerView = findViewById(R.id.rView);
        listView = findViewById(R.id.listView);
        navigationView = findViewById(R.id.naviView);
        drawerLayout = findViewById(R.id.drawLay);
        viewPager2 = findViewById(R.id.viewPage2);
        circleIndicator3 = findViewById(R.id.criIn3);
        photoList =getListPhoto();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.far_chi_tiet);

        // Khởi tạo list
        loaiSps = new ArrayList<>();
        sanPhamMoiModels = new ArrayList<>();
        if(Utils.manggiohang == null ) {
            Utils.manggiohang = new ArrayList<>();
        } else {
            int totalItem = 0 ;
            for(int i = 0; i< Utils.manggiohang.size(); i++) {
                totalItem = totalItem + Utils.manggiohang.get(i).getSo_luong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang =  new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
    }


    private List<Photo> getListPhoto (){
        List<Photo> list = new  ArrayList<>();
        list.add(new Photo(R.drawable.burger));
        list.add(new Photo(R.drawable.logo));
        list.add(new Photo(R.drawable.pizza3));
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 2500);
        int totalItem = 0 ;
        for(int i = 0; i< Utils.manggiohang.size(); i++) {
            totalItem = totalItem + Utils.manggiohang.get(i).getSo_luong();
        }
        badge.setText(String.valueOf(totalItem));
    }
    // Tạo kết nối
    private boolean isConnect (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifi != null && wifi.isConnected() || (mobile != null && mobile.isConnected())) {
                return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
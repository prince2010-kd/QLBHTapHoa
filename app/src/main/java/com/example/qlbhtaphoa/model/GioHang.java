package com.example.qlbhtaphoa.model;

public class GioHang {
    int id_sp;
    String ten_sp;
    String hinh_anh_sp;
    long gia_sp;
    int so_luong;

    public GioHang() {
    }

    public int getId_sp() {
        return id_sp;
    }

    public void setId_sp(int id_sp) {
        this.id_sp = id_sp;
    }

    public String getTen_sp() {
        return ten_sp;
    }

    public void setTen_sp(String ten_sp) {
        this.ten_sp = ten_sp;
    }

    public String getHinh_anh_sp() {
        return hinh_anh_sp;
    }

    public void setHinh_anh_sp(String hinh_anh_sp) {
        this.hinh_anh_sp = hinh_anh_sp;
    }

    public long getGia_sp() {
        return gia_sp;
    }

    public void setGia_sp(long gia_sp) {
        this.gia_sp = gia_sp;
    }

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
    }
}

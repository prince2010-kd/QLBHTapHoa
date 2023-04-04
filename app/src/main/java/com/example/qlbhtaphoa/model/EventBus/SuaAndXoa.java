package com.example.qlbhtaphoa.model.EventBus;

import com.example.qlbhtaphoa.model.SanPhamMoi;

public class SuaAndXoa {
    SanPhamMoi sanPhamMoi;

    public SuaAndXoa(SanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }

    public SanPhamMoi getSanPhamMoi() {
        return sanPhamMoi;
    }

    public void setSanPhamMoi(SanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }
}

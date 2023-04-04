package com.example.qlbhtaphoa.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlbhtaphoa.Interface.ItemImageClick;
import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.model.EventBus.TinhTong;
import com.example.qlbhtaphoa.model.GioHang;
import com.example.qlbhtaphoa.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gio_hang, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.ten_sp_gio_hang.setText(gioHang.getTen_sp());
        holder.txt_so_luong_gio_giang.setText(gioHang.getSo_luong() + " ");
        Glide.with(context).load(gioHang.getHinh_anh_sp()).into(holder.gi_hang_image);
        DecimalFormat decimalFormat  = new DecimalFormat("###,###,###");
        holder.gia_txt.setText("Giá:" + decimalFormat.format((gioHang.getGia_sp()))+ "đ");
        long gia = gioHang.getSo_luong() * gioHang.getGia_sp();
        holder.txt_gia_sp2_gio_giang.setText(decimalFormat.format(gia));
        holder.setItemImageClick(new ItemImageClick() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {

                if (giatri == 1) {
                    if(gioHangList.get(pos).getSo_luong() > 1) {
                        int soluongmoi = gioHangList.get(pos).getSo_luong() - 1;
                            gioHangList.get(pos).setSo_luong(soluongmoi);
                            holder.txt_so_luong_gio_giang.setText(gioHangList.get(pos).getSo_luong() + " ");
                        long gia = gioHangList.get(pos).getSo_luong() * gioHangList.get(pos).getGia_sp();
                            holder.txt_gia_sp2_gio_giang.setText(decimalFormat.format(gia));
                            EventBus.getDefault().postSticky(new TinhTong());
                    } else if(gioHangList.get(pos).getSo_luong() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTong());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
                else if (giatri == 2) {
                    if(gioHangList.get(pos).getSo_luong() < 11) {
                        int soluongmoi = gioHangList.get(pos).getSo_luong() + 1;
                            gioHangList.get(pos).setSo_luong(soluongmoi);
                    }
                            holder.txt_so_luong_gio_giang.setText(gioHangList.get(pos).getSo_luong() + " ");
                        long gia = gioHangList.get(pos).getSo_luong() * gioHangList.get(pos).getGia_sp();
                            holder.txt_gia_sp2_gio_giang.setText(decimalFormat.format(gia));
                            EventBus.getDefault().postSticky(new TinhTong());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView gi_hang_image, img_tru, img_cong;
        TextView ten_sp_gio_hang, gia_txt, txt_so_luong_gio_giang,txt_gia_sp2_gio_giang;
        ItemImageClick itemImageClick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gi_hang_image = itemView.findViewById(R.id.gi_hang_image);
            ten_sp_gio_hang = itemView.findViewById(R.id.ten_sp_gio_hang);
            gia_txt = itemView.findViewById(R.id.gia_txt);
            txt_so_luong_gio_giang = itemView.findViewById(R.id.txt_so_luong_gio_giang);
            txt_gia_sp2_gio_giang = itemView.findViewById(R.id.txt_gia_sp2_gio_giang);
            img_cong = itemView.findViewById(R.id.img_cong);
            img_tru = itemView.findViewById(R.id.img_tru);
            // event click
            img_cong.setOnClickListener(this);
            img_tru.setOnClickListener(this);
        }

        public void setItemImageClick(ItemImageClick itemImageClick) {
            this.itemImageClick = itemImageClick;
        }

        @Override
        public void onClick(View v) {
            if(v == img_tru) {
                itemImageClick.onImageClick(v, getAdapterPosition(), 1);

            }
            else if(v == img_cong) {
                itemImageClick.onImageClick(v, getAdapterPosition(), 2);
            }
        }
    }
}

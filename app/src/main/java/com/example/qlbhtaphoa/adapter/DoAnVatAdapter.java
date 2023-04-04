package com.example.qlbhtaphoa.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlbhtaphoa.Interface.ItemClick;
import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.activity.ChiTietActivity;
import com.example.qlbhtaphoa.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class DoAnVatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> sanPhamMoiList;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public DoAnVatAdapter(Context context, List<SanPhamMoi> sanPhamMoiList) {
        this.context = context;
        this.sanPhamMoiList = sanPhamMoiList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_do_an, parent, false);
            return new MyViewHolder(view);
        }else {
            View vieww = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loadding, parent, false);
            return new LoadingViewHodler(vieww);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyViewHolder) {
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                SanPhamMoi moi = sanPhamMoiList.get(position);
                myViewHolder.tenSp.setText(moi.getTensp());
                DecimalFormat decimalFormat  = new DecimalFormat("###,###,###");
                myViewHolder.giaSp.setText("Giá:" + decimalFormat.format(Double.parseDouble(moi.getGiasp()))+ "đ");
                myViewHolder.moTa.setText(moi.getMota());
                Glide.with(context).load(moi.getHinhanh()).into(myViewHolder.imgHinhAnh);
                myViewHolder.setItemClick(new ItemClick() {
                    @Override
                    public void OnClick(View view, int pos, boolean isLongClick) {
                        if(!isLongClick) {
                            Intent intent = new Intent(context, ChiTietActivity.class);
                            intent.putExtra("chitiet",moi);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
            }else {
                LoadingViewHodler loadingViewHodler = (LoadingViewHodler) holder;
                loadingViewHodler.progressBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemViewType(int position) {
        return sanPhamMoiList.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return sanPhamMoiList.size();
    }
    public class LoadingViewHodler extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHodler(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.proBar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tenSp, giaSp, moTa;
            ImageView imgHinhAnh;
            private ItemClick itemClick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenSp = itemView.findViewById(R.id.txt_ten_san_pham);
            giaSp = itemView.findViewById(R.id.txt_gia_san_pham);
            moTa = itemView.findViewById(R.id.txt_mo_ta);
            imgHinhAnh = itemView.findViewById(R.id.image_do_an_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.OnClick(v, getAdapterPosition(), false);
        }

        public void setItemClick(ItemClick itemClick) {
            this.itemClick = itemClick;
        }
    }

}

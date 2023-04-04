package com.example.qlbhtaphoa.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlbhtaphoa.Interface.ItemClick;
import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.activity.ChiTietActivity;
import com.example.qlbhtaphoa.model.EventBus.SuaAndXoa;
import com.example.qlbhtaphoa.model.SanPhamMoi;
import com.example.qlbhtaphoa.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> sanPhamMoiList;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> sanPhamMoiList) {
        this.context = context;
        this.sanPhamMoiList = sanPhamMoiList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi, parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myholder, int position) {
        SanPhamMoi sanPhamMoi = sanPhamMoiList.get(position);
        myholder.txtten.setText(sanPhamMoi.getTensp());
        DecimalFormat decimalFormat  = new DecimalFormat("###,###,###");
        myholder.txtgia.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+ "đ");
        if(sanPhamMoi.getHinhanh().contains("http")) {
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(myholder.imageView);
        } else {
                String hinhanh = Utils.BASE_URL + "images/"+sanPhamMoi.getHinhanh();
            Glide.with(context).load(hinhanh).into(myholder.imageView);
        }


        myholder.setItemClick(new ItemClick() {
            @Override
            public void OnClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick) {
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet",sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    EventBus.getDefault().postSticky(new SuaAndXoa(sanPhamMoi));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamMoiList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView txtgia, txtten;
        ImageView imageView;
        private ItemClick itemClick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgia = itemView.findViewById(R.id.ite_gia);
            txtten = itemView.findViewById(R.id.item_ten_spm);
            imageView = itemView.findViewById(R.id.item_spmoi_img);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClick(ItemClick itemClick) {
            this.itemClick = itemClick;
        }

        @Override
        public void onClick(View v) {
            itemClick.OnClick(v, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,getAdapterPosition(), "Sửa");
            menu.add(0,1,getAdapterPosition(),"Xóa");
        }

        @Override
        public boolean onLongClick(View v) {
            itemClick.OnClick(v, getAdapterPosition(), true);
            return false;
        }
    }
}

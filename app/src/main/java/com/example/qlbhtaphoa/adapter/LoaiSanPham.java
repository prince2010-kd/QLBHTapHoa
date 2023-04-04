package com.example.qlbhtaphoa.adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qlbhtaphoa.R;
import com.example.qlbhtaphoa.model.LoaiSp;

import java.util.List;

public class LoaiSanPham extends BaseAdapter {

    List<LoaiSp> loaiSps;
    Context context;

    public LoaiSanPham( Context context,List<LoaiSp> loaiSps) {
        this.loaiSps = loaiSps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return loaiSps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder {
        TextView viewSp;
        ImageView imageViewSp;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder  = null;
        if(view == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_san_pham, null);
            holder.viewSp = view.findViewById(R.id.item_tensp);
            holder.imageViewSp = view.findViewById(R.id.hinh_anh_san_pham);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.viewSp.setText(loaiSps.get(i).getTensanpham());
        Glide.with(context).load(loaiSps.get(i).getHinhanh()).into(holder.imageViewSp);

        return view;
    }
}

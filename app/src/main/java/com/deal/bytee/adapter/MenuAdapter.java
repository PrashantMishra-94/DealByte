package com.deal.bytee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deal.bytee.R;
import com.deal.bytee.model.menuModel;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    Context context;
    List<menuModel> menuModelList;

    public MenuAdapter(Context context, List<menuModel> menuModelList) {
        this.context = context;
        this.menuModelList = menuModelList;
    }

    public MenuAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_menu, parent, false);
        return new MenuAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        menuModel model = menuModelList.get(position);

        holder.tvName.setText(model.getName());
        holder.tvCount.setText(model.getCount());

    }

    @Override
    public int getItemCount() {
        return menuModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivBack;
        AppCompatTextView tvName;
        AppCompatTextView tvCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBack = itemView.findViewById(R.id.ivBack);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }
}

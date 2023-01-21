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
import com.deal.bytee.model.FacilityModel;
import com.deal.bytee.model.menuModel;

import java.util.List;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.MyViewHolder> {

    Context context;
    List<FacilityModel> facilityModelList;

    public FacilityAdapter(Context context, List<FacilityModel> menuModelList) {
        this.context = context;
        this.facilityModelList = menuModelList;
    }

    public FacilityAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FacilityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_facilities, parent, false);
        return new FacilityAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FacilityModel model = facilityModelList.get(position);

        holder.tvName.setText(model.getName());

        Glide.with(holder.ivImage.getContext()).load(model.getImageurl())
                .placeholder(R.drawable.logoo)
                .into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return facilityModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivImage;
        AppCompatTextView tvName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}

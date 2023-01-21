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
import com.deal.bytee.model.BusinessGallaryModel;
import com.deal.bytee.model.RestaurantModel;

import java.util.List;

public class RestaurantGalleryAdapter extends RecyclerView.Adapter<RestaurantGalleryAdapter.MyViewHolder> {

    Context context;
    List<BusinessGallaryModel> businessGallaryModelList;

    public RestaurantGalleryAdapter(Context context, List<BusinessGallaryModel> businessGallaryModelList) {
        this.context = context;
        this.businessGallaryModelList = businessGallaryModelList;
    }

    public RestaurantGalleryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantGalleryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_restaurant_gallery, parent, false);
        return new RestaurantGalleryAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantGalleryAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BusinessGallaryModel model = businessGallaryModelList.get(position);
        Glide.with(holder.ivImage.getContext()).load(model.getImage())
                .placeholder(R.drawable.logoo)
                .into(holder.ivImage);

        holder.tvName.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return businessGallaryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivImage;
        AppCompatTextView tvName;
        AppCompatTextView tvPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}

package com.deal.bytee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deal.bytee.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class HomeOfferAdapter extends RecyclerView.Adapter<HomeOfferAdapter.MyViewHolder> {

    Context context;

    public HomeOfferAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomeOfferAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_home_offer, parent, false);
        return new HomeOfferAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeOfferAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(holder.imageOffer.getContext()).load("https://tricksoffer.in/wp-content/uploads/2019/11/eatfit-food-offer.jpg")
                .thumbnail(Glide.with(context).load(R.drawable.logoo))
                .into(holder.imageOffer);

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageOffer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageOffer = itemView.findViewById(R.id.imageOffer);
        }
    }
}

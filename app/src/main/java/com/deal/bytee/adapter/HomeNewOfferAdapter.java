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
import com.deal.bytee.listnerr.RestaurantClick;
import com.deal.bytee.model.HomeBusinessModel;
import com.deal.bytee.model.RestaurantModel;

import java.util.List;
import java.util.Locale;

public class HomeNewOfferAdapter extends RecyclerView.Adapter<HomeNewOfferAdapter.MyViewHolder> {

    Context context;
    List<RestaurantModel> businessModelList;
    RestaurantClick restaurantClick;
    String type;

    public HomeNewOfferAdapter(Context context, List<RestaurantModel> businessModelList, String type) {
        this.context = context;
        this.businessModelList = businessModelList;
        this.type = type;
    }



    public void setRestaurantClick(RestaurantClick restaurantClick) {
        this.restaurantClick = restaurantClick;
    }


    @NonNull
    @Override
    public HomeNewOfferAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_home_new_offer, parent, false);
        return new HomeNewOfferAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeNewOfferAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        /*Glide.with(holder.ivImage.getContext()).load("https://static.toiimg.com/thumb/59252082.cms?width=1200&height=900")
                .thumbnail(Glide.with(context).load(R.drawable.logoo))
                .into(holder.ivImage);*/

        RestaurantModel model = businessModelList.get(position);
        Glide.with(holder.ivImage.getContext()).load(model.getImage())
                .placeholder(R.drawable.logoo)
                .into(holder.ivImage);

        holder.tvName.setText(model.getTitle());
        holder.tvDes.setText(model.getArea());
        int discount = 0;
        try {
            discount = Integer.parseInt(model.getDiscount());
        } catch (NumberFormatException | NullPointerException e) {
            //e.printStackTrace();
        }
        holder.tvDiscounttxt.setText(String.format(Locale.ENGLISH, "%d%% Discount", discount));
       // holder.tvBuyVoucher.setText(model.get());

        if (type.equals("Catering")){
           holder.tvBuyVoucher.setText("View Details");
        }else {
            holder.tvBuyVoucher.setText("Buy Voucher");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantClick.resClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return businessModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivImage;
        AppCompatTextView tvName;
        AppCompatTextView tvDes;
        AppCompatTextView tvBuyVoucher;
        AppCompatTextView tvDiscounttxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvBuyVoucher = itemView.findViewById(R.id.tvBuyVoucher);
            tvDiscounttxt = itemView.findViewById(R.id.tvDiscounttxt);
        }
    }
}

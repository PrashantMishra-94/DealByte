package com.deal.bytee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.deal.bytee.R;
import com.deal.bytee.Utils.App;
import com.deal.bytee.Utils.Endpoints;
import com.deal.bytee.Utils.MySharedPreferences;
import com.deal.bytee.Utils.MyUtils;
import com.deal.bytee.fragment.FavouriteFragment;
import com.deal.bytee.listnerr.RestaurantClick;
import com.deal.bytee.listnerr.RestaurantRemoveClick;
import com.deal.bytee.model.RestaurantModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeRestaurantAdapter extends RecyclerView.Adapter<HomeRestaurantAdapter.MyViewHolder> {

    Context context;
    String instanse;
    RestaurantClick restaurantClick;
    RestaurantRemoveClick restaurantRemoveClick;
    List<RestaurantModel> restaurantModelList;
    private Object Fragment;

    public void setRestaurantRemoveClick(RestaurantRemoveClick restaurantRemoveClick) {
        this.restaurantRemoveClick = restaurantRemoveClick;
    }

    public HomeRestaurantAdapter(Context context, String instanse, List<RestaurantModel> restaurantModelList) {
        this.context = context;
        this.instanse = instanse;
        this.restaurantModelList = restaurantModelList;
    }

    public void setRestaurantClick(RestaurantClick restaurantClick) {
        this.restaurantClick = restaurantClick;
    }

    public HomeRestaurantAdapter(Context context, String instanse) {
        this.context = context;
        this.instanse = instanse;
    }



    @NonNull
    @Override
    public HomeRestaurantAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_home_restaurant, parent, false);
        return new HomeRestaurantAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRestaurantAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (instanse.contains("FavouriteFragment")){
            holder.tvRemove.setVisibility(View.VISIBLE);
        }else {
            holder.tvRemove.setVisibility(View.GONE);
        }

        if (instanse.contains("DiscoverFragment")){
            Glide.with(holder.ivImage.getContext()).load("https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Burger_King_logo_%281999%29.svg/200px-Burger_King_logo_%281999%29.svg.png")
                    .thumbnail(Glide.with(context).load(R.drawable.logoo))
                    .into(holder.ivImage);
        }else {
            RestaurantModel model = restaurantModelList.get(position);
            Glide.with(holder.ivImage.getContext()).load(model.getImage())
                    .placeholder(R.drawable.logoo)
                    .into(holder.ivImage);

            holder.tvName.setText(model.getTitle());
            holder.tvDes.setText(model.getMain_category());
            holder.tvAddress.setText(model.getArea()+", "+model.getCity());
            int discount = 0;
            try {
                discount = Integer.parseInt(model.getDiscount());
            } catch (NumberFormatException | NullPointerException e) {
                //e.printStackTrace();
            }
            holder.tvDiscounttxt.setText(String.format(Locale.ENGLISH, "%d%% Discount", discount));
            //holder.tvDiscounttxt.setText(model.getDiscount()+"% Discount");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantClick.resClick(position);
            }
        });

        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantRemoveClick.resClick(position);
            }
        });
    }



    @Override
    public int getItemCount() {
        if (instanse.contains("DiscoverFragment") ){
            return 20;
        }else {
            return restaurantModelList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivImage;
        AppCompatTextView tvName;
        AppCompatTextView tvRemove;
        AppCompatTextView tvDes;
        AppCompatTextView tvRate;
        AppCompatTextView tvTime;
        AppCompatTextView tvShipping;
        AppCompatTextView tvAddress;
        AppCompatTextView tvDiscounttxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvRemove = itemView.findViewById(R.id.tvRemove);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvShipping = itemView.findViewById(R.id.tvShipping);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDiscounttxt = itemView.findViewById(R.id.tvDiscounttxt);
        }
    }
}

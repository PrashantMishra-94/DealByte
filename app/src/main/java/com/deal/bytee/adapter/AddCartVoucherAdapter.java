package com.deal.bytee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deal.bytee.R;
import com.deal.bytee.listnerr.RedeemVoucherClick;
import com.deal.bytee.listnerr.RemoveVoucherClick;
import com.deal.bytee.model.CartAddVoucherModel;
import com.deal.bytee.model.MyVoucherModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddCartVoucherAdapter extends RecyclerView.Adapter<AddCartVoucherAdapter.MyViewHolder> {

    Context context;
    List<CartAddVoucherModel> myVoucherModelList;
    RemoveVoucherClick removeVoucherClick;


    public void setRemoveVoucherClick(RemoveVoucherClick removeVoucherClick) {
        this.removeVoucherClick = removeVoucherClick;
    }

    public AddCartVoucherAdapter(Context context, List<CartAddVoucherModel> myVoucherModelList) {
        this.context = context;
        this.myVoucherModelList = myVoucherModelList;
    }

    public AddCartVoucherAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AddCartVoucherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_cart_voucherr, parent, false);
        return new AddCartVoucherAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AddCartVoucherAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartAddVoucherModel model = myVoucherModelList.get(position);

        holder.tvBusinessName.setText(model.getBusinessname());
        holder.tvDiscounttxt.setText(model.getDiscount()+"%");
        holder.tvAmount.setText(String.valueOf(model.getAmount()));

        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeVoucherClick.removeClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myVoucherModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvBusinessName;
        AppCompatTextView tvDiscounttxt;
        AppCompatTextView tvAmount;
        AppCompatTextView tvRemove;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBusinessName = itemView.findViewById(R.id.tvBusinessName);
            tvDiscounttxt = itemView.findViewById(R.id.tvDiscounttxt);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvRemove = itemView.findViewById(R.id.tvRemove);

        }
    }
}

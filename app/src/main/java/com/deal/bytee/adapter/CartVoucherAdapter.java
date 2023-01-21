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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deal.bytee.R;
import com.deal.bytee.listnerr.RedeemVoucherClick;
import com.deal.bytee.model.MyVoucherModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartVoucherAdapter extends RecyclerView.Adapter<CartVoucherAdapter.MyViewHolder> {

    Context context;
    List<MyVoucherModel> myVoucherModelList;
    RedeemVoucherClick redeemVoucherClick;

    public void setRedeemVoucherClick(RedeemVoucherClick redeemVoucherClick) {
        this.redeemVoucherClick = redeemVoucherClick;
    }

    public CartVoucherAdapter(Context context, List<MyVoucherModel> myVoucherModelList) {
        this.context = context;
        this.myVoucherModelList = myVoucherModelList;
    }

    public CartVoucherAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CartVoucherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_cart_voucher, parent, false);
        return new CartVoucherAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartVoucherAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MyVoucherModel model = myVoucherModelList.get(position);

        holder.tvName.setText(model.getCv_business_title());
        holder.tvCode.setText(model.getCv_code());
        holder.tvPrice.setText(model.getCv_price());
        holder.tvExDate.setText(model.getCv_expiry_date());
        holder.tvStatus.setText(model.getCv_status());
        holder.tvPaidAmount.setText("Rs."+model.getCv_amount_applied());

        if (model.getCv_status().equals("Not used")){
            holder.llRedeem.setVisibility(View.VISIBLE);
            holder.llpaid.setVisibility(View.GONE);
            holder.llViewDetails.setVisibility(View.GONE);
        }else {
            holder.llRedeem.setVisibility(View.GONE);
            holder.llpaid.setVisibility(View.VISIBLE);
            holder.llViewDetails.setVisibility(View.VISIBLE);
        }

        holder.btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(holder.edtAmount.getText().toString().trim())){
                    holder.edtAmount.requestFocus();
                    holder.edtAmount.setError("Please enter Amount!");
                }else {
                    redeemVoucherClick.redeemClick(position,holder.edtAmount.getText().toString());
                }
            }
        });


        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Transaction Details")
                        .setContentText("Total Bill Amount: Rs."+(model.getDiscountamount()+model.getCv_amount_applied())+"\nDiscount: "+model.getCv_discount()+"\nPayable Amount: Rs."+model.getCv_amount_applied())
                        .setConfirmText("OKAY")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return myVoucherModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;
        AppCompatTextView tvCode;
        AppCompatTextView tvPrice;
        AppCompatTextView tvExDate;
        AppCompatTextView tvStatus;
        AppCompatTextView tvPaidAmount;
        LinearLayoutCompat llRedeem;
        LinearLayoutCompat llpaid;
        LinearLayoutCompat llViewDetails;
        AppCompatEditText edtAmount;
        AppCompatButton btnUse;
        AppCompatButton btnViewDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnUse = itemView.findViewById(R.id.btnUse);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            edtAmount = itemView.findViewById(R.id.edtAmount);
            llRedeem = itemView.findViewById(R.id.llRedeem);
            llpaid = itemView.findViewById(R.id.llpaid);
            llViewDetails = itemView.findViewById(R.id.llViewDetails);
            tvName = itemView.findViewById(R.id.tvName);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPaidAmount = itemView.findViewById(R.id.tvPaidAmount);
            tvExDate = itemView.findViewById(R.id.tvExDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}

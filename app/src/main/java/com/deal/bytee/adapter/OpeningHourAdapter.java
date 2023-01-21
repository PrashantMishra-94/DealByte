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
import com.deal.bytee.model.OpeningModel;

import java.util.List;

public class OpeningHourAdapter extends RecyclerView.Adapter<OpeningHourAdapter.MyViewHolder> {

    Context context;
    List<OpeningModel> facilityModelList;

    public OpeningHourAdapter(Context context, List<OpeningModel> menuModelList) {
        this.context = context;
        this.facilityModelList = menuModelList;
    }

    public OpeningHourAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OpeningHourAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_opening_hours, parent, false);
        return new OpeningHourAdapter.MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OpeningHourAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OpeningModel model = facilityModelList.get(position);

        holder.tvName.setText(model.getDayname());
        holder.tvTime.setText(model.getDayname());
    }

    @Override
    public int getItemCount() {
        return facilityModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;
        AppCompatTextView tvTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
